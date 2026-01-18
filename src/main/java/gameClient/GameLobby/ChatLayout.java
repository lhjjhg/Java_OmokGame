package gameClient.GameLobby;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

import Function.MessageDTO;
import Function.MessageType;
import gameClient.ClientBack;
import gameClient.Main.MainFrame;
import gameClient.Main.WeatherFrame;

public class ChatLayout extends JFrame {
	String nickName;
	ClientBack clientBack = new ClientBack();

	DefaultListModel<String> roomModel;
	DefaultListModel<String> userModel;

	JPopupMenu pm = new JPopupMenu();
	JMenuItem pmItem1 = new JMenuItem("Enter room");
	JMenuItem pmItem2 = new JMenuItem("Remove room");

	private JPopupMenu userPopupMenu;

	private static final long serialVersionUID = 1L;

	/* Panel */
	private JPanel MainPanel;
	private JPanel ChatPanel;
	private JPanel UserPanel;
	private JPanel RoomPanel;
	/* Field */
	private JTextField ChatField;
	/* TextArea */
	public JTextPane ChatPane; // JTextArea → JTextPane
	public JTextArea HelloArea;
	/* List */
	public JList<String> RoomList;
	public JList<String> UserList;
	/* Label */
	private JLabel UserL;
	private JLabel RoomL;
	private JLabel ProfileL;
	private JLabel characterLabel; // 캐릭터 이미지 라벨 추가
	/* Button */
	private JButton sendBtn;
	private JButton EmoticonBtn;
	private JButton exitBtn;
	private JButton NewRoomBtn;
	private JButton changeCharacterBtn; // 캐릭터 변경 버튼 추가
	private JButton searchMessageBtn; // 메세지 검색 버튼 추가
	private JButton rankingBtn;
	private JButton weatherBtn;
	private JButton paletteBtn;
	private JButton darkModeBtn;
	
	private boolean isDarkMode = false; // 현재 다크 모드 상태

	private LineBorder bb = new LineBorder(Color.black, 2, false);
	private Color selectedColor = Color.BLACK; // 기본 텍스트 색상
	
	public String getNickName() {
		return this.nickName;
	}

	public void resetNickName(String newNickName) {
		this.nickName = newNickName;
	}

	public ChatLayout(String nickName, String ipAddress, int portNum) {

		this.nickName = nickName;

		setTitle("전체 채팅");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 916, 600);

		/* Panel 설정 */
		MainPanel = new JPanel();
		MainPanel.setBackground(new Color(250, 240, 230));
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);
		MainPanel.setLayout(null);

		ChatPanel = new JPanel();
		ChatPanel.setBackground(SystemColor.textHighlightText);
		ChatPanel.setBounds(10, 10, 485, 491);
		MainPanel.add(ChatPanel);
		ChatPanel.setLayout(null);

		UserPanel = new JPanel();
		UserPanel.setBackground(SystemColor.window);
		UserPanel.setBounds(505, 62, 211, 209);
		MainPanel.add(UserPanel);
		UserPanel.setLayout(null);

		RoomPanel = new JPanel();
		RoomPanel.setBackground(SystemColor.textHighlightText);
		RoomPanel.setBounds(505, 281, 211, 220);
		MainPanel.add(RoomPanel);
		RoomPanel.setLayout(null);

		/* TextArea 설정 */
		ChatPane = new JTextPane();
		ChatPane.setText("");
		ChatPane.setBounds(-164, 18, 427, 426);
		ChatPane.setEditable(false); // 입력 불가능 설정
		ChatPane.setBorder(bb);
		ChatPanel.add(ChatPane);

		// Wrap ChatArea in a JScrollPane
		JScrollPane scrollPane = new JScrollPane(ChatPane);
		scrollPane.setBounds(10, 53, 465, 428); // Set the bounds for the scroll pane

		// Add the scroll pane to the chat panel instead of ChatArea
		ChatPanel.add(scrollPane);

		HelloArea = new JTextArea();
		HelloArea.setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
		HelloArea.setBounds(60, 18, 415, 35);
		HelloArea.setEditable(false);
		ChatPanel.add(HelloArea);
		HelloArea.append(nickName);

		userModel = new DefaultListModel<>(); // DefaultListModel을 생성
		UserList = new JList<>(userModel); // JList 생성
		UserList.setBounds(10, 35, 189, 164);
		UserList.setBorder(bb);
		UserPanel.add(UserList);

		RoomList = new JList<>();
		RoomList.setBounds(10, 28, 191, 182);
		RoomList.setBorder(bb);
		RoomPanel.add(RoomList);

		/* Field 설정 */
		ChatField = new JTextField();
		ChatField.setBounds(59, 511, 436, 44);
		ChatField.setBorder(bb);
		ChatField.setColumns(10);
		MainPanel.add(ChatField);

		/* Label 설정 */
		UserL = new JLabel("접속중인 유저");
		UserL.setFont(new Font("한컴산뜻돋움", Font.BOLD, 12));
		UserL.setBounds(10, 10, 87, 15);
		UserPanel.add(UserL);

		RoomL = new JLabel("방 목록");
		RoomL.setFont(new Font("한컴산뜻돋움", Font.BOLD, 12));
		RoomL.setBounds(10, 10, 50, 15);
		RoomPanel.add(RoomL);

		ImageIcon originalIcon = new ImageIcon(ChatLayout.class.getResource("/Image/사용자1.png"));
		Image originalImage = originalIcon.getImage();
		Image resizedImage = originalImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);

		ProfileL = new JLabel(resizedIcon);
		ProfileL.setBounds(10, 10, 40, 40);
		ChatPanel.add(ProfileL);

		// 캐릭터 이미지 라벨
		characterLabel = new JLabel();
		characterLabel.setBounds(737, 52, 150, 150);
		MainPanel.add(characterLabel);
		characterLabel.setOpaque(true); // 배경 활성화
		characterLabel.setBackground(new Color(255, 250, 205)); // 옅은 노란색 배경
		characterLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3)); // 황금색 테두리

		JLabel characterTitleLabel = new JLabel("내 캐릭터");
		characterTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		characterTitleLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 16));
		characterTitleLabel.setBounds(737, 20, 150, 30); // 위치 조정
		MainPanel.add(characterTitleLabel);

		/* Button 설정 */
		// 보내기 버튼
		sendBtn = new JButton("보내기");
		sendBtn.setBounds(569, 511, 121, 44);
		styleButton(sendBtn); // 공통 스타일 적용
		MainPanel.add(sendBtn);

//		// 이모티콘 버튼
//		EmoticonBtn = new JButton("이모티콘");
//		EmoticonBtn.setBounds(505, 511, 91, 44);
//		styleButton(EmoticonBtn); // 공통 스타일 적용
//		MainPanel.add(EmoticonBtn);
		
		// 이모티콘 버튼
		EmoticonBtn = new JButton();
		EmoticonBtn.setBounds(505, 511, 44, 44); // 크기를 아이콘에 맞게 조정

		// 팔레트 이미지 추가
		ImageIcon paletteIcon = new ImageIcon(getClass().getClassLoader().getResource("Image/행복.png"));
		Image scaledPaletteImage = paletteIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		EmoticonBtn.setIcon(new ImageIcon(scaledPaletteImage)); // 버튼에 이미지 아이콘 추가

		// 버튼 스타일 제거
		EmoticonBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // 검은색 테두리 추가
		EmoticonBtn.setContentAreaFilled(false); // 배경 제거
		EmoticonBtn.setFocusPainted(false); // 포커스 테두리 제거
		EmoticonBtn.setOpaque(false); // 투명하게 설정

		// 버튼 클릭 시 효과를 위해 손 모양 커서 추가
		EmoticonBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// 마우스 호버 효과 추가
		EmoticonBtn.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        // 마우스가 버튼 위에 올라갔을 때
		        EmoticonBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // 테두리를 파란색으로 변경
		        EmoticonBtn.setContentAreaFilled(true); // 배경 활성화
		        EmoticonBtn.setBackground(new Color(220, 220, 220)); // 연한 파란 배경색 추가
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        // 마우스가 버튼에서 나갔을 때
		        EmoticonBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // 원래 검은 테두리로 복원
		        EmoticonBtn.setContentAreaFilled(false); // 배경 제거
		    }
		});

		MainPanel.add(EmoticonBtn);


		// 방 생성하기 버튼
		NewRoomBtn = new JButton("방 생성하기");
		NewRoomBtn.setBounds(737, 457, 150, 44);
		styleButton(NewRoomBtn); // 공통 스타일 적용
		MainPanel.add(NewRoomBtn);

		// 나가기 버튼
		exitBtn = new JButton("로그아웃");
		exitBtn.setBounds(737, 510, 150, 44);
		styleButton(exitBtn); // 공통 스타일 적용
		MainPanel.add(exitBtn);

		// 캐릭터 변경 버튼
		changeCharacterBtn = new JButton("캐릭터 변경");
		changeCharacterBtn.setBounds(737, 212, 150, 36);
		MainPanel.add(changeCharacterBtn);
		styleButton(changeCharacterBtn);

		// 메세지 검색 버튼
		searchMessageBtn = new JButton("메시지 검색");
		searchMessageBtn.setBounds(612, 10, 104, 42); // 위치 설정
		styleButton(searchMessageBtn); // 공통 스타일 적용
		MainPanel.add(searchMessageBtn);

		// 다크 모드 버튼
		darkModeBtn = new JButton("다크 모드");
		darkModeBtn.setBounds(505, 10, 97, 42); // 위치 설정
		styleButton(darkModeBtn); // 공통 스타일 적용
		MainPanel.add(darkModeBtn);

		// 순위 버튼
		rankingBtn = new JButton("순위");
		rankingBtn.setBounds(737, 403, 150, 44); // 다크 모드 버튼 위
		styleButton(rankingBtn); // 공통 스타일 적용
		MainPanel.add(rankingBtn);

		/* addActionListner */
		ChatField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = ChatField.getText().trim();
				if (message.length() > 0) {
					// MessageDTO 생성 후 전송
					clientBack.sendMessage(new MessageDTO(MessageType.CHAT, "[" + nickName + "]: " + message));
					ChatField.setText(null);
				}
			}
		});

		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = ChatField.getText().trim();
				if (e.getSource() == sendBtn && message.length() > 0) {
					// MessageDTO 생성 후 전송
					clientBack.sendMessage(new MessageDTO(MessageType.CHAT, "[" + nickName + "]: " + message));
					ChatField.setText(null);
				}
			}
		});

		roomModel = new DefaultListModel<>();
		RoomList.setModel(roomModel);

		EmoticonBtn.addActionListener(e -> {
			EmoticonSelector selector = new EmoticonSelector(ChatLayout.this);
			selector.setVisible(true);

			String selectedEmoticon = selector.getSelectedEmoticonCode();
			if (selectedEmoticon != null) {
				// 서버로 메시지 전송 (자신의 화면에는 즉시 추가하지 않음)
				clientBack.sendMessage(new MessageDTO(MessageType.EMOJI, nickName + ":" + selectedEmoticon));
			}
		});
		
		  /* 방 생성 버튼 동작 */
        NewRoomBtn.addActionListener(e -> {
            String roomName = JOptionPane.showInputDialog("방 이름을 입력하세요.");
            if (roomName != null && !roomName.isEmpty()) {
                // OmokCreateRoom 메시지 전송
                clientBack.sendMessage(new MessageDTO(MessageType.OmokCreateRoom, nickName + ":" + roomName));
            }
        });

		RoomList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					String selectedRoom = RoomList.getSelectedValue();
					if (selectedRoom != null) {
						// MessageDTO 생성 후 전송
						clientBack.sendMessage(new MessageDTO(MessageType.OmokEnterRoom, nickName + ":" + selectedRoom));
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					pm.show(RoomList, e.getX(), e.getY());
				}
			}
		});

		pm.add(pmItem1);
		pmItem1.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String selectedRoom = RoomList.getSelectedValue();
		        if (selectedRoom != null) {
		            clientBack.sendMessage(new MessageDTO(MessageType.OmokEnterRoom, nickName + ":" + selectedRoom));
		        } else {
		            JOptionPane.showMessageDialog(null, "선택된 방이 없습니다.");
		        }
		    }
		});

		pm.add(pmItem2);
		pmItem2.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String selectedRoom = RoomList.getSelectedValue();
		        if (selectedRoom != null) {
		            clientBack.sendMessage(new MessageDTO(MessageType.OmokRemoveRoom, selectedRoom));
		        } else {
		            JOptionPane.showMessageDialog(null, "선택된 방이 없습니다.");
		        }
		    }
		});

		getContentPane().add(pm);
		
		// 날씨 버튼 추가
		weatherBtn = new JButton("오늘의 날씨");
		weatherBtn.setBounds(737, 349, 150, 44); // 위치 설정
		styleButton(weatherBtn); // 버튼 스타일 적용
		MainPanel.add(weatherBtn);
		
		// 팔레트 버튼 설정
		paletteBtn = new JButton();
		paletteBtn.setBounds(10, 511, 44, 44); // 크기를 아이콘에 맞게 조정

		// 팔레트 이미지 추가
		ImageIcon paletteIcon1 = new ImageIcon(getClass().getResource("/Image/팔레트1.png"));
		Image scaledPaletteImage1 = paletteIcon1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		paletteBtn.setIcon(new ImageIcon(scaledPaletteImage1)); // 버튼에 이미지 아이콘 추가

		// 버튼 스타일 제거
		paletteBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // 검은색 테두리 추가
		paletteBtn.setContentAreaFilled(false); // 배경 제거
		paletteBtn.setFocusPainted(false); // 포커스 테두리 제거
		paletteBtn.setOpaque(false); // 투명하게 설정

		// 버튼 클릭 시 효과를 위해 손 모양 커서 추가
		paletteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// 마우스 호버 효과 추가
		paletteBtn.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        // 마우스가 버튼 위에 올라갔을 때
		        paletteBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // 테두리를 파란색으로 변경
		        paletteBtn.setContentAreaFilled(true); // 배경 활성화
		        paletteBtn.setBackground(new Color(220, 220, 220)); // 연한 파란 배경색 추가
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        // 마우스가 버튼에서 나갔을 때
		        paletteBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // 원래 검은 테두리로 복원
		        paletteBtn.setContentAreaFilled(false); // 배경 제거
		    }
		});

		// 팔레트 버튼 액션 추가
		paletteBtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // JColorChooser로 색상 선택
		        Color color = JColorChooser.showDialog(ChatLayout.this, "색상을 선택하세요", selectedColor);
		        if (color != null) {
		            selectedColor = color; // 선택된 색상 저장
		            JOptionPane.showMessageDialog(ChatLayout.this, "색상이 변경되었습니다!", "알림", JOptionPane.INFORMATION_MESSAGE);
		        }
		    }
		});

		MainPanel.add(paletteBtn);
		
		JButton btnBgm = new JButton("BGM");
		btnBgm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnBgm.setBounds(737, 295, 150, 44);
		MainPanel.add(btnBgm);
		styleButton(btnBgm);

		// 날씨 버튼 클릭 시 WeatherFrame 호출
		weatherBtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // WeatherFrame 표시
		        new WeatherFrame().setVisible(true);
		    }
		});

		userPopupMenu = new JPopupMenu();

		JMenuItem oneOnOneChatItem = new JMenuItem("1대1 대화하기");
		JMenuItem profileInfoItem = new JMenuItem("프로필 정보");

		userPopupMenu.add(oneOnOneChatItem);
		userPopupMenu.add(profileInfoItem);

		// 사용자 목록에 마우스 리스너 추가
		UserList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
					int index = UserList.locationToIndex(e.getPoint());
					if (index >= 0) {
						UserList.setSelectedIndex(index);
						userPopupMenu.show(UserList, e.getX(), e.getY());
					}
				}
			}
		});

		/* 1대1 채팅 옵션 추가 */
		oneOnOneChatItem.addActionListener(e -> {
			String selectedUser = UserList.getSelectedValue();
			if (selectedUser != null && !selectedUser.equals(nickName)) {
				// 1대1 채팅 요청 메시지를 서버로 전송
				clientBack.sendMessage(new MessageDTO(MessageType.OneToOneChatRequest, nickName + ":" + selectedUser));
			} else {
				JOptionPane.showMessageDialog(this, "올바른 유저를 선택하세요.");
			}
		});

		/* 프로필창 옵션 */
		profileInfoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedUser = UserList.getSelectedValue(); // 선택된 사용자 가져오기
				if (selectedUser != null) {
					new ProfileFrame(selectedUser).setVisible(true); // 프로필 창 열기
				} else {
					JOptionPane.showMessageDialog(ChatLayout.this, "사용자를 선택해 주세요.");
				}
			}
		});

		ChatField.addMouseListener(new MouseAdapter() {
		});
		ChatField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (ChatField.getText().equals("채팅을 입력하세요")) {
					ChatField.setText("");
				}
			}
		});

		ChatField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (ChatField.getText().equals("채팅을 입력하세요")) {
					ChatField.setText("");
				}
			}
		});

		// 나가기 버튼
		exitBtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        int confirm = JOptionPane.showConfirmDialog(
		            ChatLayout.this,
		            "정말로 종료하시겠습니까?",
		            "종료 확인",
		            JOptionPane.YES_NO_OPTION
		        );

		        if (confirm == JOptionPane.YES_OPTION) {
		            try {
		                // 서버에 LeaveUser 메시지 전송
		                clientBack.sendMessage(new MessageDTO(MessageType.LeaveUser, nickName));
		                // 클라이언트 연결 종료
		                clientBack.disconnect();
		                // 현재 창 닫고 메인 화면으로 이동
		                dispose();
		                new MainFrame(portNum);
		            } catch (Exception ex) {
		                System.out.println("Error during client exit: " + ex.getMessage());
		            }
		        }
		    }
		});


		changeCharacterBtn.addActionListener(e -> {
			new CharacterSelectFrame(nickName, "localhost", portNum, this); // 현재 ChatLayout 전달
		});

		// 메시지 검색 버튼 클릭 시
		searchMessageBtn.addActionListener(e -> {
			new MessageSearchFrame(nickName); // 닉네임 전달
		});

		// 순위 버튼 클릭 이벤트
		rankingBtn.addActionListener(e -> new RankingFrame());

		// 다크 모드 버튼 클릭 시 이벤트 처리
		darkModeBtn.addActionListener(e -> toggleDarkMode());
		
		// 창 닫기 버튼 동작 추가
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showConfirmDialog(
		            ChatLayout.this,
		            "정말로 종료하시겠습니까?",
		            "종료 확인",
		            JOptionPane.YES_NO_OPTION
		        );

		        if (confirm == JOptionPane.YES_OPTION) {
		            try {
		                // 서버에 LeaveUser 메시지 전송
		                clientBack.sendMessage(new MessageDTO(MessageType.LeaveUser, nickName));
		                // 클라이언트 연결 종료
		                clientBack.disconnect();
		                // 창 닫기
		                dispose();
		                new MainFrame(portNum); // 메인 화면으로 이동 (필요 시)
		            } catch (Exception ex) {
		                System.out.println("Error during client exit: " + ex.getMessage());
		            }
		        }
		    }
		});

		clientBack.setGui(this);
		clientBack.setUserInfo(nickName, ipAddress, portNum);
		clientBack.start(); // 채팅창이 켜짐과 동시에 접속을 실행해줍니다.

	}

	/* 메서드 */
	// 일반 메시지 추가 메서드
	public void appendMessage(String message) {
		StyledDocument doc = ChatPane.getStyledDocument();

		try {
			// 일반 메시지 추가
			doc.insertString(doc.getLength(), message + "\n", null);
			ChatPane.setCaretPosition(doc.getLength()); // 스크롤 하단 이동
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resetRoomList(ArrayList<String> roomNameList) {
		roomModel.removeAllElements();
		RoomList.removeAll();
		roomModel.clear();

		if (roomNameList.size() == 1 && roomNameList.get(0).equals("EMPTY")) {
			// 방 목록이 비어있는 경우
			return;
		}
		for (String roomName : roomNameList) {
			// roomModel.addElement(roomName);
			addRoom(roomName);
		}
	}

	public void resetUserList(ArrayList<String> nickNameList) {
		userModel.removeAllElements(); // 기존 리스트 초기화
		UserList.removeAll(); // UI에서 리스트 제거

		userModel.clear(); // 모델 초기화
		for (String userName : nickNameList) {
			addUser(userName); // 새로운 유저 추가
		}

		UserList.setModel(userModel); // 모델 갱신
		UserList.repaint(); // UI 다시 그리기
	}

	public void addUser(String userName) {
		if (!userModel.contains(userName)) { // 중복 방지
			userModel.addElement(userName);
			UserList.repaint(); // 리스트 업데이트
		}
	}

	public void removeUser(String userName) {
		if (userModel.contains(userName)) {
			userModel.removeElement(userName);
			UserList.repaint(); // 리스트 업데이트
		}
	}

	public void addRoom(String roomName) {
		if (!roomModel.contains(roomName)) {
			roomModel.addElement(roomName);
			RoomList.repaint(); // 리스트 업데이트
		}
	}

	public void removeRoom(String roomName) {
		if (roomModel.contains(roomName)) {
			roomModel.removeElement(roomName);
			RoomList.repaint(); // 리스트 업데이트
		}
	}

	// 이모티콘 코드 → 이미지 경로 변환
	private String convertEmoticonCodeToPath(String emoticonCode) {
		switch (emoticonCode) {
		case "(행복)":
			return "/Image/행복.png";
		case "(슬픔)":
			return "/Image/슬픔.png";
		case "(분노)":
			return "/Image/분노.png";
		case "(사랑)":
			return "/Image/사랑.png";
		case "(놀람)":
			return "/Image/놀람.png";
		case "(무표정)":
			return "/Image/무표정.png";
		default:
			return null;
		}
	}

	private ImageIcon resizeIcon(String imagePath, int width, int height) {
		try {
			ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
			Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return new ImageIcon(resizedImage);
		} catch (Exception e) {
			System.err.println("이미지를 조정하는 중 오류 발생: " + e.getMessage());
			return null;
		}
	}

	public void appendEmoticonToChatPane(String nickName, String emoticonCode) {
		StyledDocument doc = ChatPane.getStyledDocument();
		Style style = ChatPane.addStyle("Emoticon", null);

		try {
			// 닉네임 추가
			doc.insertString(doc.getLength(), nickName + ": ", null);

			// 이모티콘 이미지 추가
			String imagePath = convertEmoticonCodeToPath(emoticonCode);
			if (imagePath != null) {
				ImageIcon resizedIcon = resizeIcon(imagePath, 50, 50);
				if (resizedIcon != null) {
					StyleConstants.setIcon(style, resizedIcon);
					doc.insertString(doc.getLength(), "Ignored", style); // 이미지 삽입
				}
			} else {
				doc.insertString(doc.getLength(), emoticonCode, null); // 텍스트로 표시
			}

			// 줄바꿈 추가
			doc.insertString(doc.getLength(), "\n", null);
			ChatPane.setCaretPosition(doc.getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 공통 스타일 메서드 */
	private void styleButton(JButton button) {
		button.setFocusPainted(false); // 포커스 테두리 제거
		button.setBackground(isDarkMode ? new Color(80, 80, 80) : new Color(245, 245, 245)); // 다크/라이트 모드 배경색
		button.setForeground(isDarkMode ? Color.WHITE : Color.BLACK); // 다크/라이트 모드 텍스트 색상
		button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // 라이트 그레이 테두리
		button.setFont(new Font("맑은 고딕", Font.BOLD, 14)); // 폰트 설정
		button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 손 모양 커서

		// 호버 효과 추가
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(isDarkMode ? new Color(100, 100, 100) : new Color(230, 230, 230)); // 호버 색상
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(isDarkMode ? new Color(80, 80, 80) : new Color(245, 245, 245)); // 원래 배경색 복원
			}
		});
	}

	public void setCharacterImage(String characterPath) {
		try {
			ImageIcon characterIcon = new ImageIcon(getClass().getResource(characterPath));
			Image scaledImage = characterIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
			characterLabel.setIcon(new ImageIcon(scaledImage));
		} catch (Exception e) {
			System.err.println("캐릭터 이미지 로드 실패: " + e.getMessage());
			characterLabel.setIcon(null); // 기본 이미지를 설정하거나 null로 처리
		}
	}

	// 다크 모드 활성화/비활성화 메서드
	private void toggleDarkMode() {
		isDarkMode = !isDarkMode; // 다크 모드 상태 토글

		// 다크 모드 색상 설정
		Color bgColor = isDarkMode ? new Color(45, 45, 45) : new Color(250, 240, 230);
		Color panelColor = isDarkMode ? new Color(60, 60, 60) : SystemColor.textHighlightText;
		Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;
		Color btnBgColor = isDarkMode ? new Color(80, 80, 80) : new Color(245, 245, 245);

		// 메인 패널 배경색 변경
		MainPanel.setBackground(bgColor);

		// 패널 색상 변경
		ChatPanel.setBackground(panelColor);
		UserPanel.setBackground(panelColor);
		RoomPanel.setBackground(panelColor);

		// 텍스트 필드 및 텍스트 영역 색상 변경
		ChatField.setBackground(isDarkMode ? new Color(50, 50, 50) : Color.WHITE);
		ChatField.setForeground(textColor);
		ChatField.setCaretColor(textColor); // 커서 색상
		HelloArea.setBackground(panelColor);
		HelloArea.setForeground(textColor);
		ChatPane.setBackground(isDarkMode ? new Color(50, 50, 50) : Color.WHITE);
		ChatPane.setForeground(textColor);

		// 버튼 색상 변경
		styleButton(darkModeBtn);
		styleButton(sendBtn);
		styleButton(EmoticonBtn);
		styleButton(NewRoomBtn);
		styleButton(exitBtn);
		styleButton(changeCharacterBtn);
		styleButton(searchMessageBtn);
		styleButton(rankingBtn);
		styleButton(weatherBtn); // 날씨 버튼 다크 모드 적용
		
		// 유저 및 방 리스트 배경색 변경
		UserList.setBackground(isDarkMode ? new Color(50, 50, 50) : Color.WHITE);
		UserList.setForeground(textColor);
		RoomList.setBackground(isDarkMode ? new Color(50, 50, 50) : Color.WHITE);
		RoomList.setForeground(textColor);

		// 라벨 텍스트 색상 변경
		UserL.setForeground(textColor); // "접속중인 유저" 라벨
		RoomL.setForeground(textColor); // "방 목록" 라벨
		JLabel characterTitleLabel = (JLabel) MainPanel.getComponentAt(737, 20); // "내 캐릭터" 라벨
		if (characterTitleLabel != null) {
			characterTitleLabel.setForeground(textColor);
		}

		// 다크 모드 버튼 텍스트 변경
		darkModeBtn.setText(isDarkMode ? "라이트 모드" : "다크 모드");

		// UI 다시 그리기
		MainPanel.repaint();
		MainPanel.revalidate();
	}
}
