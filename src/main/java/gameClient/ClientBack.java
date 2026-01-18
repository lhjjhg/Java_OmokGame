package gameClient;

import DB.Database;
import Function.Data;
import Function.MessageDTO;
import Function.MessageType;
import gameClient.GameLobby.ChatLayout;
import gameClient.GameLobby.OneChatLayout;

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

public class ClientBack extends Thread {
	private String nickName, roomName, ipAddress;
	private int portNum;
	private ChatLayout chatLayout;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private ArrayList<String> nickNameList = new ArrayList<>();
	private ArrayList<String> roomNameList = new ArrayList<>();
	private volatile boolean running = true;
	// 1대1 채팅 레이아웃을 저장
	private OneChatLayout oneChatLayout;
	private OmokGameLayout omokGameLayout;

	public String getNickName() {
		return nickName;
	}

	public void resetNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setGui(ChatLayout chatLayout) {
		if (chatLayout == null) {
			System.out.println("ChatLayout is null!");
		} else {
			this.chatLayout = chatLayout;
		}
	}

	public void setUserInfo(String nickName, String ipAddress, int portNum) {
		this.nickName = nickName;
		this.ipAddress = ipAddress;
		this.portNum = portNum;
	}

	public void disconnect() {
		try {
			running = false; // 플래그를 설정하여 스레드를 종료
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
			System.out.println("Disconnected from the server.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			socket = new Socket(ipAddress, portNum);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

			// 닉네임을 포함한 첫 메시지 전송
			sendMessage(new MessageDTO(MessageType.EnterUser, nickName));

			while (running) { // running 플래그 체크
				MessageDTO receivedMessage = (MessageDTO) in.readObject();
				handleReceivedMessage(receivedMessage);
			}
		} catch (IOException | ClassNotFoundException e) {
			if (running) { // 소켓이 정상적으로 종료된 경우는 에러를 무시
				System.out.println("Error: " + e.getMessage());
			}
		} finally {
			disconnect(); // 소켓을 안전하게 닫음
		}
	}

	private void handleReceivedMessage(MessageDTO message) {
		try {
			switch (message.getType()) {
			case ResetUserList:
				updateUserList(message.getContent());
				break;
			case ResetRoomList:
			    handleResetRoomList(message.getContent());
			    break;
			case UserLeft:
				chatLayout.appendMessage("[알림]: " + message.getContent()); // 채팅창에 퇴장 메시지 표시
				break;
			case EMOJI:
				System.out.println("[클라이언트]: EMOJI 메시지 수신 - " + message.getContent());
				String emojiMessage = message.getContent();
				String[] parts = emojiMessage.split(":");
				if (parts.length == 2) {
					String senderNick = parts[0];
					String emoticonCode = parts[1];
					chatLayout.appendEmoticonToChatPane(senderNick, emoticonCode);
				}
				break;
			case OneToOneChatRequest:
				handleOneToOneChatRequest(message.getContent()); // 1대1 채팅방 요청
				break;
			case EnterPrivateRoom:
				handleEnterPrivateRoom(message.getContent()); // 1대1 채팅방 입장
				break;
			case PrivateChat:
				handlePrivateChatMessage(message.getContent()); // 1대1 채팅방 채팅 메세지
				break;
			case CHAT:
				chatLayout.appendMessage(message.getContent());
				break;
			case OmokCreateRoom:
				handleOmokCreateRoom(message.getContent()); // 오목 게임방 생성 
				break;
			case OmokEnterRoom:
                handleEnterRoom(message.getContent()); // 오목 게임방 입장 
                break;
			case OmokRemoveRoom:
				handleOmokRemoveRoom(message.getContent()); // 오목 게임방 삭제 
				break;
			case OmokUpdateRoomUserList: 
				handleOmokUpdateRoomUserList(message.getContent(), message.getObject()); // 오목 게임방 유저 리스트 업데이트 
				break;
			case OmokLeaveRoom:
			    handleOmokLeaveRoom(message.getContent());
			    break;
			case OmokSurrender:
			    handleOmokSurrender(message.getContent());
			    break;
			case OmokUpdatePlayer:
			    handleOmokUpdatePlayer(message.getContent());
			    break;
			case OmokChat:
			    handleOmokChat(message.getContent());
			    break;
			case OmokReady:
			    handleOmokReady(message.getContent());
			    break;
			case OmokStart:
			    handleOmokStart(message.getContent());
			    break;
			case OmokPlaceStone:
			    handleOmokPlaceStone(message.getObject());
			    break;
			default:
				System.out.println("Unhandled message type: " + message.getType());
			}
		} catch (Exception e) {
			System.out.println("Error handling message: " + e.getMessage());
		}
	}

	// 오목 방 생성
	private void handleOmokCreateRoom(String roomName) {
		if (!roomNameList.contains(roomName)) {
			roomNameList.add(roomName);
			chatLayout.addRoom(roomName); // UI에 새 방 추가
			System.out.println("[디버그]: 새로운 방 생성 - " + roomName);
		} else {
			System.err.println("[오류]: 이미 존재하는 방 - " + roomName);
		}
	}
	// 오목 방 입장 
	private void handleEnterRoom(String content) {
	    String[] enterParts = content.split(":");
	    if (enterParts.length == 3) { // 데이터 검증
	        String user = enterParts[0];
	        String roomName = enterParts[1];
	        String role = enterParts[2];

	        // 현재 유저가 입장하는 경우만 UI 생성
	        if (nickName.equals(user)) {
	            if (omokGameLayout == null || !omokGameLayout.getRoomName().equals(roomName)) {
	                omokGameLayout = new OmokGameLayout(nickName, roomName, role, this);
	                System.out.println("[디버그]: 방 입장 성공 - User: " + user + ", Room: " + roomName + ", Role: " + role);
	            } else {
	                System.err.println("[오류]: 이미 생성된 방에 다시 입장 시도");
	            }
	        } else {
	            System.err.println("[오류]: 방 입장 처리 중 예상치 못한 데이터 - " + content);
	        }
	    } else {
	        System.err.println("오류: EnterRoom 메시지 형식이 잘못되었습니다 - " + content);
	    }
	}

	// 오목 방 제거
	private void handleOmokRemoveRoom(String roomName) {
	    if (roomNameList.contains(roomName)) {
	        roomNameList.remove(roomName);
	        chatLayout.removeRoom(roomName); // UI에서 방 제거
	        System.out.println("[디버그]: 방 제거 - " + roomName);

	        // OmokGameLayout 창 닫기
	        if (omokGameLayout != null && omokGameLayout.getRoomName().equals(roomName)) {
	            omokGameLayout.dispose();
	            omokGameLayout = null;
	        }
	    } else {
	        System.err.println("[오류]: 제거하려는 방이 목록에 없습니다 - " + roomName);
	    }
	}
	// 오목 방 유저 리스트 업데이트 
	private void handleOmokUpdateRoomUserList(String roomName, Object userListObject) {
	    if (roomName == null || userListObject == null) {
	        System.err.println("[클라이언트]: OmokUpdateRoomUserList 메시지 데이터가 null");
	        return;
	    }

	    if (userListObject instanceof List) {
	        @SuppressWarnings("unchecked")
	        List<String> userList = (List<String>) userListObject;
	        if (omokGameLayout != null && omokGameLayout.getRoomName().equals(roomName)) {
	            omokGameLayout.updateUserList(userList); // OmokGameLayout의 유저 리스트 업데이트
	            System.out.println("[디버그]: 유저 리스트 업데이트 - Room: " + roomName + ", Users: " + userList);
	        } else {
	            System.err.println("[오류]: OmokGameLayout이 null이거나 방 이름이 일치하지 않습니다.");
	        }
	    } else {
	        System.err.println("[클라이언트]: 유효하지 않은 유저 리스트 데이터");
	    }
	}
	// 오목 게임방 목록 업데이트 
	private void handleResetRoomList(String updatedRoomList) {
	    if (updatedRoomList == null || updatedRoomList.isEmpty()) {
	        roomNameList.clear();
	        chatLayout.resetRoomList(new ArrayList<>()); // 빈 리스트 전달
	        return;
	    }

	    ArrayList<String> newRoomNameList = new ArrayList<>(Arrays.asList(updatedRoomList.split(",")));
	    roomNameList.clear();
	    roomNameList.addAll(newRoomNameList);

	    chatLayout.resetRoomList(newRoomNameList); // ChatLayout에 업데이트된 방 목록 전달
	    System.out.println("[디버그]: 방 목록 업데이트 - " + newRoomNameList);
	}

	private void handleOmokLeaveRoom(String content) {
	    if (omokGameLayout != null && omokGameLayout.getRoomName().equals(content)) {
	        omokGameLayout.dispose();
	        omokGameLayout = null;
	        System.out.println("[클라이언트]: 방에서 나갔습니다 - " + content);
	    }
	}

	private void handleOmokSurrender(String content) {
	    if (omokGameLayout != null) {
	        omokGameLayout.appendChatMessage("[서버]: 상대방이 항복했습니다. 게임 종료.");
	        omokGameLayout.dispose();
	        omokGameLayout = null;
	    }
	}

	private void handleOmokUpdatePlayer(String content) {
	    String[] parts = content.split(":");
	    String player = parts[0];
	    String role = parts[1];

	    if (omokGameLayout != null) {
	        omokGameLayout.updatePlayerRole(player, role);
	        System.out.println("[클라이언트]: 플레이어 상태 업데이트 - " + player + " -> " + role);
	    }
	}

	private void handleOmokChat(String content) {
	    String[] parts = content.split(":");
	    if (parts.length == 3) { // roomName:nickName:message
	        String sender = parts[1];
	        String message = parts[2];
	        if (omokGameLayout != null) {
	            omokGameLayout.appendChatMessage(sender + ": " + message);
	        }
	    } else {
	        System.err.println("[오류]: 잘못된 채팅 메시지 형식 - " + content);
	    }
	}

	private void handleOmokReady(String content) {
	    String[] parts = content.split(":");
	    String player = parts[0];
	    String status = parts[1]; // "준비완료" 또는 "취소"

	    if (omokGameLayout != null) {
	        boolean isReady = "준비완료".equals(status);
	        omokGameLayout.updateReadyStatus(player, isReady);
	        System.out.println("[클라이언트]: 준비 상태 업데이트 - " + player + ": " + status);
	    }
	}

	private void handleOmokStart(String content) {
	    if (omokGameLayout != null) {
	        omokGameLayout.startGame();
	        System.out.println("[클라이언트]: 게임 시작 - 방 이름: " + content);
	    }
	}

	private void handleOmokPlaceStone(Object gameState) {
	    if (gameState instanceof int[]) {
	        int[] position = (int[]) gameState;
	        if (omokGameLayout != null) {
	            omokGameLayout.updateBoard(position, Data.myChess == 1);
	        }
	    } else {
	        System.err.println("[클라이언트]: 잘못된 오목 돌 위치 데이터");
	    }
	}

	// ---------------------------------------------------------------- //
	// 1대1 채팅 요청 
	private void handleOneToOneChatRequest(String content) {
		String[] parts = content.split(":");
		String sender = parts[0];

		// 수락 여부 확인
		int result = JOptionPane.showConfirmDialog(null, sender + "님이 1대1 채팅을 요청했습니다. 수락하시겠습니까?", "1대1 채팅 요청",
				JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			// 수락한 경우 서버에 수락 메시지 전송
			sendMessage(new MessageDTO(MessageType.EnterPrivateRoom, sender + ":" + nickName));
		} else {
			// 거절한 경우 상대방에게 거절 알림
			sendMessage(new MessageDTO(MessageType.CHAT, "[알림]: " + sender + "님의 1대1 채팅 요청을 거절했습니다."));
		}
	}
	// 1대1 채팅 입장 
	private void handleEnterPrivateRoom(String content) {
		String[] parts = content.split(":");
		String sender = parts[0];
		String recipient = parts[1];

		// 채팅방은 요청을 수락한 쪽과 요청한 쪽 모두에서 열림
		if (nickName.equals(sender) || nickName.equals(recipient)) {
			oneChatLayout = new OneChatLayout(nickName.equals(sender) ? recipient : sender, // 상대방 닉네임
					nickName, // 내 닉네임
					this // 클라이언트 백엔드
			);
			oneChatLayout.setVisible(true);
		}
	}
	// 1대1 채팅창 설정 
	private void handlePrivateChatMessage(String content) {
		String[] parts = content.split(":");
		String sender = parts[0];
		String message = parts[1];

		// 내가 보낸 메시지는 isSender를 true로 설정
		boolean isSender = sender.equals(nickName);

		if (oneChatLayout != null) {
			oneChatLayout.appendMessage(isSender ? sender + ": " + message : "나: " + message, isSender);
		}
	}
	// 유저 리스트 업데이트 
	private void updateUserList(String updatedUserList) {
		ArrayList<String> newNickNameList = new ArrayList<>(Arrays.asList(updatedUserList.split(",")));
		ArrayList<String> addedUsers = new ArrayList<>(newNickNameList);
		ArrayList<String> removedUsers = new ArrayList<>(nickNameList);

		addedUsers.removeAll(nickNameList); // 새롭게 추가된 사용자
		removedUsers.removeAll(newNickNameList); // 제거된 사용자

		for (String user : addedUsers) {
			chatLayout.addUser(user); // UI에 새 사용자 추가
		}

		for (String user : removedUsers) {
			chatLayout.removeUser(user); // UI에서 사용자 제거
		}

		nickNameList = newNickNameList; // 기존 목록을 새 목록으로 업데이트
	}

	public void sendMessage(MessageDTO message) {
		try {
			out.writeObject(message);
			out.flush();

			// 메시지 저장
			if (message.getType() == MessageType.CHAT) {
				saveChatMessageDB(message.getContent());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveChatMessageDB(String messageContent) {
		try {
			String[] parts = messageContent.split(": ", 2);
			if (parts.length == 2) {
				String senderNick = parts[0].replace("[", "").replace("]", "");
				Database db = new Database();
				db.saveChatMessage(roomName == null ? "로비" : roomName, senderNick, parts[1]);
				db.close();
			}
		} catch (Exception e) {
			System.out.println("Error saving message to database: " + e.getMessage());
		}
	}
}