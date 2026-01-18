package gameServer;

import Function.MessageDTO;
import Function.MessageType;

import java.net.*;
import java.io.*;
import java.util.*;

import DB.Database;

public class ServerBack extends Thread {
	private int portNum;
	private int subPortNum = 1;
	private ServerSocket serversocket;
	private Vector<ReceiveThread> clientThreadList = new Vector<>();
	private ArrayList<String> nickNameList = new ArrayList<>();
	private ArrayList<String> roomNameList = new ArrayList<>();
	private HashMap<String, ServerBack> roomMap = new HashMap<>();
	private Map<String, OmokRoom> omokRooms = new HashMap<>(); // 오목 게임 관리하는 맵 추가
	private Socket socket;

	public ServerBack(int portNum) {
		this.portNum = portNum;
		runServer();
		start();
	}

	// 오목 게임방 정보를 저장할 클래스 생성
	class OmokRoom {
		String roomName;
		String host;
		List<String> players = new ArrayList<>(); // 최대 2명
		List<String> observers = new ArrayList<>(); // 관전자
		Set<String> readyPlayers = new HashSet<>(); 

		public OmokRoom(String roomName, String host) {
			this.roomName = roomName;
			this.host = host;
			this.players.add(host); // 방장이 첫 번째 플레이어
		}
	}

	public void runServer() {
		try {
			Collections.synchronizedList(clientThreadList);
			serversocket = new ServerSocket(portNum);
			System.out.println("현재 아이피와 포트넘버는 [" + InetAddress.getLocalHost() + "], [" + portNum + "] 입니다.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void run() {
		try {
			while (true) {
				System.out.println("새 접속을 대기합니다...");
				socket = serversocket.accept();
				System.out.println("[" + socket.getInetAddress() + "]에서 접속하셨습니다.");
				ReceiveThread receiveThread = new ReceiveThread(socket);
				clientThreadList.add(receiveThread);
				receiveThread.start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void sendAll(MessageDTO message) {
		for (ReceiveThread clientThread : clientThreadList) {
			try {
				clientThread.sendMessage(message);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public ArrayList<String> getNickNameList() {
		return nickNameList;
	}

	public int getPortNum() {
		return portNum;
	}

	class ReceiveThread extends Thread {
		private Socket socket; // 소켓 필드 추가
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private String nickName;

		public ReceiveThread(Socket socket) {
			this.socket = socket; // 소켓 초기화
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());

				// 닉네임 수신
				MessageDTO initialMessage = (MessageDTO) in.readObject();
				if (initialMessage.getType() == MessageType.EnterUser) {
					nickName = initialMessage.getContent();
					nickNameList.add(nickName);
					System.out.println(nickNameList);
				}
			} catch (IOException | ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}

		public void run() {
			try {
				sendAll(new MessageDTO(MessageType.CHAT, "[서버]: " + nickName + "님이 입장하셨습니다."));
				updateUserAndRoomLists();

				while (true) {
					MessageDTO receivedMessage = (MessageDTO) in.readObject();
					switch (receivedMessage.getType()) {
					case LeaveUser: // 클라이언트 연결 종료 처리
						handleClientExit(nickName);
						return; // 스레드 종료
					case EMOJI:
						System.out.println("[서버]: EMOJI 메시지 수신 - " + receivedMessage.getContent());
						sendAll(receivedMessage);
						break;
					case OneToOneChatRequest:
						createPrivateChatRoom(receivedMessage.getContent());
						break;
					case EnterPrivateRoom:
						handleEnterPrivateRoom(receivedMessage.getContent());
						break;
					case PrivateChat:
						handlePrivateChat(receivedMessage.getContent());
						break;
					case CHAT:
						sendAll(receivedMessage);
						break;
					case OmokCreateRoom:
						handleOmokCreateRoom(receivedMessage.getContent()); // 여기서부터 오목
						break;
					case OmokEnterRoom:
						handleOmokEnterRoom(receivedMessage.getContent());
						break;
					case OmokRemoveRoom:
						handleOmokRemoveRoom(receivedMessage.getContent());
						break;
					case OmokLeaveRoom:
					    handleOmokLeaveRoom(receivedMessage.getContent());
					    break;
					case OmokSurrender:
					    handleOmokSurrender(receivedMessage.getContent());
					    break;
					case OmokUpdatePlayer:
					    handleOmokUpdatePlayer(receivedMessage.getContent());
					    break;
					case OmokChat:
					    handleOmokChat(receivedMessage.getContent());
					    break;
					case OmokReady:
					    handleOmokReady(receivedMessage.getContent());
					    break;
					case OmokStart:
					    handleOmokStart(receivedMessage.getContent());
					    break;
					case OmokPlaceStone:
					    handleOmokPlaceStone(receivedMessage.getContent(), receivedMessage.getObject());
					    break;
					default:
						System.out.println("알 수 없는 메시지 타입: " + receivedMessage.getType());
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("[서버]: " + nickName + " 연결 종료 - " + e.getMessage());
			}
		}

		private void handleOmokCreateRoom(String content) {
		    String[] parts = content.split(":");
		    String creator = parts[0];
		    String roomName = parts[1];

		    if (!omokRooms.containsKey(roomName)) {
		        OmokRoom newRoom = new OmokRoom(roomName, creator);
		        omokRooms.put(roomName, newRoom);
		        roomNameList.add(roomName);

		        // 방 생성 후 자동 입장 처리
		        handleOmokEnterRoom(creator + ":" + roomName);

		        sendAll(new MessageDTO(MessageType.OmokCreateRoom, roomName));
		    }
		}

		 private void handleOmokEnterRoom(String content) {
			    String[] parts = content.split(":");
			    String user = parts[0];
			    String roomName = parts[1];

			    if (omokRooms.containsKey(roomName)) {
			        OmokRoom room = omokRooms.get(roomName);

			        if (room.players.size() < 2) {
			            room.players.add(user);
			            sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokEnterRoom, user + ":" + roomName + ":PLAYER"));
			        } else {
			            room.observers.add(user);
			            sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokEnterRoom, user + ":" + roomName + ":OBSERVER"));
			        }

			        updateOmokRoomUserList(room);
			    } else {
			        System.out.println("[서버]: 존재하지 않는 방 입장 요청 - " + roomName);
			        findClientThreadByNickName(user).sendMessage(
			            new MessageDTO(MessageType.CHAT, "[알림]: 존재하지 않는 방입니다. 다시 시도해주세요."));
			    }
			}

		 private void handleOmokRemoveRoom(String roomName) {
			    if (omokRooms.containsKey(roomName)) {
			        omokRooms.remove(roomName);
			        roomNameList.remove(roomName);

			        // 모든 클라이언트에게 방 제거 정보 전송
			        sendAll(new MessageDTO(MessageType.OmokRemoveRoom, roomName));

			        // 방 목록 동기화
			        updateUserAndRoomLists();

			        System.out.println("[서버]: 방 제거 완료 - " + roomName);
			    } else {
			        System.out.println("[서버]: 방 제거 실패 - 존재하지 않는 방: " + roomName);
			    }
			}

		 private void updateOmokRoomUserList(OmokRoom room) {
			    Set<String> allUsers = new HashSet<>(room.players);
			    allUsers.addAll(room.observers);

			    if (allUsers.isEmpty()) {
			        System.err.println("[서버]: 업데이트할 유저 리스트가 비어있음");
			        return;
			    }

			    MessageDTO updateMessage = new MessageDTO(
			        MessageType.OmokUpdateRoomUserList,
			        room.roomName,
			        new ArrayList<>(allUsers)
			    );

			    sendMessageToRoom(room.roomName, updateMessage);
			}


		 private void sendMessageToRoom(String roomName, MessageDTO message) {
			    OmokRoom room = omokRooms.get(roomName);
			    if (room != null) {
			        Set<String> allUsers = new HashSet<>();
			        allUsers.addAll(room.players);
			        allUsers.addAll(room.observers);

			        for (String user : allUsers) {
			            findClientThreadByNickName(user).sendMessage(message);
			        }
			    }
			}
		 
		 private void handleOmokLeaveRoom(String content) {
			    String[] parts = content.split(":");
			    String user = parts[0];
			    String roomName = parts[1];

			    if (omokRooms.containsKey(roomName)) {
			        OmokRoom room = omokRooms.get(roomName);

			        // 방장이 나가는 경우
			        if (room.host.equals(user)) {
			            if (room.players.size() > 1) {
			                // 플레이어2를 방장으로 승격
			                String newHost = room.players.get(1);
			                room.host = newHost;
			                room.players.remove(newHost);
			                room.players.add(0, newHost); // 새 방장을 플레이어1로 설정
			                sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokUpdatePlayer, newHost + ":" + roomName + ":PLAYER"));
			            } else if (!room.observers.isEmpty()) {
			                // 관전자를 방장으로 승격
			                String newHost = room.observers.get(0);
			                room.host = newHost;
			                room.observers.remove(newHost);
			                room.players.add(newHost); // 새 방장을 플레이어1로 설정
			                sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokUpdatePlayer, newHost + ":" + roomName + ":PLAYER"));
			            } else {
			                // 방에 남아있는 유저가 없으면 방 삭제
			                handleOmokRemoveRoom(roomName);
			                return;
			            }
			        } else {
			            // 방장이 아닌 플레이어나 관전자가 나가는 경우
			            room.players.remove(user);
			            room.observers.remove(user);

			            if (room.players.size() < 2 && !room.observers.isEmpty()) {
			                // 관전자를 플레이어로 승격
			                String newPlayer = room.observers.get(0);
			                room.observers.remove(newPlayer);
			                room.players.add(newPlayer);
			                sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokUpdatePlayer, newPlayer + ":" + roomName + ":PLAYER"));
			            }
			        }

			        updateOmokRoomUserList(room);
			    }
			}

			private void handleOmokSurrender(String content) {
			    String[] parts = content.split(":");
			    String user = parts[0];
			    String roomName = parts[1];

			    if (omokRooms.containsKey(roomName)) {
			        OmokRoom room = omokRooms.get(roomName);

			        for (String player : room.players) {
			            if (!player.equals(user)) {
			                sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokSurrender, player + "님의 승리!"));
			                break;
			            }
			        }
			        handleOmokRemoveRoom(roomName);
			    }
			}

			private void handleOmokUpdatePlayer(String content) {
			    String[] parts = content.split(":");
			    String user = parts[0];
			    String roomName = parts[1];
			    String newRole = parts[2];

			    if (omokRooms.containsKey(roomName)) {
			        OmokRoom room = omokRooms.get(roomName);
			        room.players.remove(user);
			        room.observers.remove(user);

			        if (newRole.equals("PLAYER") && room.players.size() < 2) {
			            room.players.add(user);
			        } else {
			            room.observers.add(user);
			        }

			        sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokUpdatePlayer, content));
			        updateOmokRoomUserList(room);
			    }
			}

			private void handleOmokChat(String content) {
			    String[] parts = content.split(":");
			    String roomName = parts[0];
			    String message = parts[1];

			    sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokChat, message));
			}

			private void handleOmokReady(String content) {
			    String[] parts = content.split(":");
			    String user = parts[0];
			    String roomName = parts[1];
			    boolean cancelReady = parts.length > 2 && parts[2].equals("취소");

			    if (omokRooms.containsKey(roomName)) {
			        OmokRoom room = omokRooms.get(roomName);

			        if (cancelReady) {
			            room.readyPlayers.remove(user);
			        } else {
			            room.readyPlayers.add(user);
			        }

			        // 준비 상태 업데이트 메시지 전송
			        sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokReady, user + ":" + (cancelReady ? "취소" : "준비완료")));

			        // 모든 플레이어가 준비되면 게임 시작
			        if (room.readyPlayers.containsAll(room.players) && room.players.size() == 2) {
			            handleOmokStart(roomName);
			        }
			    }
			}

			private void handleOmokStart(String roomName) {
			    if (omokRooms.containsKey(roomName)) {
			        OmokRoom room = omokRooms.get(roomName);

			        // 게임 시작 메시지 전송
			        sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokStart, roomName));

			        // 준비 상태 초기화
			        room.readyPlayers.clear();
			    }
			}

			private void handleOmokPlaceStone(String content, Object stonePosition) {
			    String[] parts = content.split(":");
			    String user = parts[0];
			    String roomName = parts[1];

			    if (omokRooms.containsKey(roomName)) {
			        sendMessageToRoom(roomName, new MessageDTO(MessageType.OmokPlaceStone, user + ":" + roomName, stonePosition));
			    }
			}

		// ----------------------------------------------------------------- //
		// 1대1 채팅방 생성 메서드
		private void createPrivateChatRoom(String content) {
			String[] parts = content.split(":");
			String sender = parts[0];
			String recipient = parts[1];
			ReceiveThread recipientThread = findClientThreadByNickName(recipient);

			if (recipientThread != null) {
				// 요청 메시지를 상대방에게 전달
				recipientThread.sendMessage(new MessageDTO(MessageType.OneToOneChatRequest, sender + ":" + recipient));
			} else {
				// 상대방이 접속 중이 아니면 알림 전송
				findClientThreadByNickName(sender)
						.sendMessage(new MessageDTO(MessageType.CHAT, "[서버]: 상대방이 현재 접속 중이 아닙니다."));
			}
		}

		private void handleEnterPrivateRoom(String content) {
			String[] parts = content.split(":");
			String sender = parts[0];
			String recipient = parts[1];

			ReceiveThread senderThread = findClientThreadByNickName(sender);
			ReceiveThread recipientThread = findClientThreadByNickName(recipient);

			if (senderThread != null && recipientThread != null) {
				senderThread.sendMessage(new MessageDTO(MessageType.EnterPrivateRoom, sender + ":" + recipient));
				recipientThread.sendMessage(new MessageDTO(MessageType.EnterPrivateRoom, sender + ":" + recipient));
			}
		}

		// 1대1 채팅 메시지 처리
		private void handlePrivateChat(String content) {
			String[] parts = content.split(":");
			String sender = parts[0];
			String recipient = parts[1];
			String message = parts[2];

			ReceiveThread senderThread = findClientThreadByNickName(sender);
			ReceiveThread recipientThread = findClientThreadByNickName(recipient);

			if (recipientThread != null) {
				// 수신자에게 메시지 전송 (송신자를 포함한 메시지)
				recipientThread.sendMessage(new MessageDTO(MessageType.PrivateChat, sender + ":" + message));
			}

			if (senderThread != null) {
				// 송신자에게 메시지 전송 (송신자를 포함한 메시지)
				senderThread.sendMessage(new MessageDTO(MessageType.PrivateChat, sender + ":" + message));
			}
		}

		// 특정 닉네임의 클라이언트 스레드 검색
		private ReceiveThread findClientThreadByNickName(String nickName) {
			for (ReceiveThread client : clientThreadList) {
				if (client.nickName.equals(nickName)) {
					return client;
				}
			}
			return null;
		}

		private void updateUserAndRoomLists() {
			// 유저 리스트 갱신 메시지
			sendAll(new MessageDTO(MessageType.ResetUserList, String.join(",", nickNameList)));
			// 방 리스트 갱신 메시지
			sendAll(new MessageDTO(MessageType.ResetRoomList, String.join(",", roomNameList)));
		}

		private void handleClientExit(String nickName) {
			if (nickNameList.contains(nickName)) { // 중복 처리 방지
				removeClient(this, nickName); // 클라이언트 스레드 제거
				sendAll(new MessageDTO(MessageType.UserLeft, nickName + "님이 퇴장하셨습니다.")); // 퇴장 메시지 브로드캐스트
				updateUserAndRoomLists(); // 유저 목록 갱신
			}
		}

		public void removeClient(ReceiveThread client, String nickName) {
			try {
				clientThreadList.remove(client);
				nickNameList.remove(nickName);
				if (!client.socket.isClosed()) { // 소켓이 닫히지 않은 경우만 처리
					client.socket.close();
				}
				System.out.println("[서버]: " + nickName + "님이 연결을 종료했습니다.");
			} catch (Exception e) {
				System.out.println("Error removing client: " + e.getMessage());
			}
		}
		
		public void sendMessage(MessageDTO message) {
			try {
				out.writeObject(message);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}