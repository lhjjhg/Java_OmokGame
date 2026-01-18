package gameClient;

import javax.swing.*;
import javax.swing.border.LineBorder;

import DB.Database;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import Function.Data;
import Function.MessageDTO;
import Function.MessageType;

public class OmokGameLayout extends JFrame {
	private String nickName;
	private String roomName;
	private String role; // PLAYER or OBSERVER
	private ClientBack clientBack;

	private BoardCanvas boardCanvas;
	private JTextArea chatArea;
	private JTextField inputField;
	private JButton readyBtn, surrenderBtn, leaveBtn, startGameBtn;

	private JLabel player1Label, player2Label;
	private JLabel player1Character, player2Character;
	private JLabel player1Status, player2Status; // 준비 상태 표시

	private DefaultListModel<String> userModel;
	private JList<String> userList;

	private boolean isReady = false; // 현재 유저의 준비 상태

	public OmokGameLayout(String nickName, String roomName, String role, ClientBack clientBack) {
		this.nickName = nickName;
		this.roomName = roomName;
		this.role = role;
		this.clientBack = clientBack;

		System.out.println(
				"[디버그]: OmokGameLayout 생성 - NickName: " + nickName + ", RoomName: " + roomName + ", Role: " + role);

		setTitle("오목 게임방 - " + roomName);
		setSize(1000, 650);
		setLayout(null);

		// 오목 판 추가
		boardCanvas = new BoardCanvas(clientBack, roomName, nickName);
		boardCanvas.setBounds(10, 10, 531, 531);
		add(boardCanvas);
		  // MouseListener 추가: 클릭한 위치에 돌 배치
		boardCanvas.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		        // 클릭 좌표를 보드 위치로 변환
		        int x = (e.getX() - 10) / 35; // 보드 이미지의 시작 좌표를 정확히 보정
		        int y = (e.getY() - 10) / 35;

		        // 좌표가 유효한 범위인지 확인
		        if (x >= 0 && x < 15 && y >= 0 && y < 15) {
		            // 빈 칸인지 확인
		            if (Data.chessBoard[y][x] == 0) { // y, x 순서 유의
		                // 현재 턴의 돌을 놓음
		                Data.chessBoard[y][x] = (Data.turn == 0) ? Data.BLACK : Data.WHITE;
		                Data.last = y * 15 + x; // 마지막 돌 위치 저장

		                // 돌 이미지 갱신
		                boardCanvas.paintBoardImage();
		                boardCanvas.repaint();

		                // 턴 변경
		                Data.turn = (Data.turn == 0) ? 1 : 0;

		                System.out.println("[정보]: 돌을 놓았습니다. 위치: (" + x + ", " + y + "), 현재 턴: " +
		                        (Data.turn == 0 ? "흑돌" : "백돌"));
		            } else {
		                System.out.println("[오류]: 이미 돌이 놓인 위치입니다!");
		            }
		        } else {
		            System.out.println("[오류]: 보드 범위를 벗어났습니다!");
		        }
		    }
		});

		// 플레이어 정보 표시
		player1Label = new JLabel("플레이어 1");
		player1Label.setBounds(560, 10, 200, 30);
		add(player1Label);

		player1Character = new JLabel();
		player1Character.setBounds(560, 50, 150, 150);
		player1Character.setBorder(new LineBorder(Color.BLACK, 2));
		add(player1Character);

		player1Status = new JLabel("게임 중"); // 초기 상태
		player1Status.setForeground(Color.BLUE);
		player1Status.setBounds(560, 210, 150, 30);
		add(player1Status);

		player2Label = new JLabel("플레이어 2");
		player2Label.setBounds(760, 10, 200, 30);
		add(player2Label);

		player2Character = new JLabel();
		player2Character.setBounds(760, 50, 150, 150);
		player2Character.setBorder(new LineBorder(Color.BLACK, 2));
		add(player2Character);

		player2Status = new JLabel("게임 중"); // 초기 상태
		player2Status.setForeground(Color.BLUE);
		player2Status.setBounds(760, 210, 150, 30);
		add(player2Status);

		// 유저 리스트
		userModel = new DefaultListModel<>();
		userList = new JList<>(userModel);
		JScrollPane userScrollPane = new JScrollPane(userList);
		userScrollPane.setBounds(560, 260, 350, 100);
		add(userScrollPane);

		// 채팅 영역
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane.setBounds(560, 370, 350, 150);
		add(chatScrollPane);
		
		// 초기 메시지 추가
        chatArea.append("하트: 안녕\n");
        chatArea.append("유저2: 게임 준비 눌러\n");

		inputField = new JTextField();
		inputField.setBounds(560, 530, 260, 30);
		add(inputField);
		inputField.addActionListener(e -> sendChatMessage());

		// 버튼들
		readyBtn = new JButton("준비");
		readyBtn.setBounds(830, 530, 80, 30);
		add(readyBtn);

		surrenderBtn = new JButton("항복");
		surrenderBtn.setBounds(560, 570, 80, 30);
		add(surrenderBtn);

		leaveBtn = new JButton("나가기");
		leaveBtn.setBounds(650, 570, 80, 30);
		add(leaveBtn);

		startGameBtn = new JButton("게임 시작");
		startGameBtn.setBounds(740, 570, 120, 30);
		add(startGameBtn);

		// 버튼 동작
		readyBtn.addActionListener(e -> {
		    if ("준비".equals(readyBtn.getText())) {
		        // 준비 상태로 변경
		        clientBack.sendMessage(new MessageDTO(MessageType.OmokReady, roomName + ":" + nickName + ":ready"));
		        readyBtn.setText("준비완료");
		    } else {
		        // 준비 해제 상태로 변경
		        clientBack.sendMessage(new MessageDTO(MessageType.OmokReady, roomName + ":" + nickName + ":취소"));
		        readyBtn.setText("준비");
		    }
		});

		 surrenderBtn.addActionListener(e -> {
	            int result = JOptionPane.showConfirmDialog(this, "정말 항복하시겠습니까?", "항복 확인",
	                    JOptionPane.YES_NO_OPTION);
	            if (result == JOptionPane.YES_OPTION) {
	                clientBack.sendMessage(new MessageDTO(MessageType.OmokSurrender, nickName + ":" + roomName));
	            }
	        });

		leaveBtn.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, "관전자는 게임에 참여할 수 없습니다.");
		});

		startGameBtn.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, "흑돌이 승리하였습니다.");
			clientBack.sendMessage(new MessageDTO(MessageType.OmokStart, roomName));
		});

		setVisible(true);
	}

	public String getRoomName() {
		return roomName;
	}

	public void updateUserList(List<String> users) {
	    userModel.clear();
	    Database db = new Database();

	    // 플레이어 정보 초기화
	    player1Label.setText("플레이어 1");
	    player1Character.setIcon(null);
	    player2Label.setText("플레이어 2");
	    player2Character.setIcon(null);

	    boolean isPlayer1Set = false;

	    for (String user : users) {
	        String[] profile = db.getProfile(user);
	        if (profile != null) {
	            String userName = profile[0];
	            String characterPath = profile[2];

	            // 플레이어 1 설정
	            if (!isPlayer1Set) {
	                player1Label.setText(userName);
	                updateCharacterImage(player1Character, characterPath);
	                isPlayer1Set = true;
	            }
	            // 플레이어 2 설정
	            else {
	                player2Label.setText(userName);
	                updateCharacterImage(player2Character, characterPath);
	            }

	            userModel.addElement(userName);
	        } else {
	            userModel.addElement(user); // 프로필 정보를 찾지 못하면 닉네임만 표시
	        }
	    }

	    db.close();
	    System.out.println("[디버그]: 업데이트된 유저 리스트: " + users);
	}

	private void sendChatMessage() {
		String message = inputField.getText().trim();
		if (!message.isEmpty()) {
			clientBack.sendMessage(new MessageDTO(MessageType.OmokChat, roomName + ":" + nickName + ":" + message));
			inputField.setText("");
		}
	}

	public void appendChatMessage(String message) {
		chatArea.append(message + "\n");
	}

	public void updatePlayerInfo(String player1, String player2, String player1CharacterPath,
			String player2CharacterPath) {
		player1Label.setText(player1 != null ? player1 : "플레이어 1");
		player2Label.setText(player2 != null ? player2 : "플레이어 2");

		updateCharacterImage(player1Character, player1CharacterPath);
		updateCharacterImage(player2Character, player2CharacterPath);
	}

	// 캐릭터 이미지 업데이트 메서드
	private void updateCharacterImage(JLabel characterLabel, String characterPath) {
		try {
			if (characterPath != null && !characterPath.isEmpty()) {
				ImageIcon characterIcon = new ImageIcon(getClass().getResource(characterPath));
				Image scaledImage = characterIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
				characterLabel.setIcon(new ImageIcon(scaledImage));
			} else {
				characterLabel.setIcon(null); // 기본 이미지 처리
			}
		} catch (Exception e) {
			System.err.println("캐릭터 이미지 로드 실패: " + e.getMessage());
			characterLabel.setIcon(null);
		}
	}

	public void updateReadyStatus(String playerName, boolean ready) {
	    if (playerName.equals(player1Label.getText())) {
	        player1Status.setText(ready ? "준비 완료" : "준비 상태");
	    } else if (playerName.equals(player2Label.getText())) {
	        player2Status.setText(ready ? "준비 완료" : "준비 상태");
	    }

	    // 두 플레이어가 모두 준비 완료 시 게임 시작 버튼 활성화
	    boolean bothReady = "준비 완료".equals(player1Status.getText()) && "준비 완료".equals(player2Status.getText());
	    startGameBtn.setEnabled(bothReady);
	}

	
	public void updatePlayerRole(String playerName, String role) {
	    if (playerName.equals(player1Label.getText())) {
	        if ("PLAYER".equals(role)) {
	            player1Status.setText("플레이어");
	        } else {
	            player1Status.setText("관전자");
	        }
	    } else if (playerName.equals(player2Label.getText())) {
	        if ("PLAYER".equals(role)) {
	            player2Status.setText("플레이어");
	        } else {
	            player2Status.setText("관전자");
	        }
	    }
	}
	
	public void startGame() {
	    chatArea.append("[알림]: 게임이 시작되었습니다!\n");
	    readyBtn.setEnabled(false); // 준비 버튼 비활성화
	    startGameBtn.setEnabled(false); // 게임 시작 버튼 비활성화
	    surrenderBtn.setEnabled(true); // 항복 버튼 활성화

	    // 보드 초기화 (필요시)
	    Data.chessBoard = new int[15][15];
	    boardCanvas.paintBoardImage();
	    boardCanvas.repaint();
	}

	public void updateBoard(int[] position, boolean isMyStone) {
		Data.chessBoard[position[0]][position[1]] = isMyStone ? Data.myChess : Data.oppoChess;
		boardCanvas.paintBoardImage();
		boardCanvas.repaint();
	}
	
	
}