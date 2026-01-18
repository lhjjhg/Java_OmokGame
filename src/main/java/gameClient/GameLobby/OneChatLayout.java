package gameClient.GameLobby;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import gameClient.ClientBack;

import Function.MessageDTO;
import Function.MessageType;

public class OneChatLayout extends JFrame {
	private String sender, recipient;
	private JTextPane chatPane;
	private JTextField chatField;
	private JButton sendButton, paletteButton;
	private ClientBack clientBack;
	private Color textColor = Color.BLACK; // 기본 텍스트 색상

	public OneChatLayout(String sender, String recipient, ClientBack clientBack) {
		this.sender = sender;
		this.recipient = recipient;
		this.clientBack = clientBack;

		// 프레임 설정
		setTitle("1:1 대화 - " + recipient);
		setSize(450, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // 중앙 배치

		// 메인 패널 설정
		JPanel mainPanel = new JPanel(new BorderLayout(0, 0)); // 여백을 없애기 위해 BorderLayout(0, 0) 설정
		mainPanel.setBackground(new Color(250, 240, 230)); // 메인 배경색
		add(mainPanel);

		// 채팅 영역
		chatPane = new JTextPane();
		chatPane.setEditable(false);
		chatPane.setBackground(new Color(240, 240, 240)); // 채팅 배경색
		chatPane.setBorder(null); // 테두리 제거
		JScrollPane chatScrollPane = new JScrollPane(chatPane);
		chatScrollPane.setBorder(null); // 스크롤 영역 테두리 제거
		mainPanel.add(chatScrollPane, BorderLayout.CENTER);

		// 입력 영역
		JPanel inputPanel = new JPanel(new BorderLayout(0, 0)); // 여백을 없애기 위해 0, 0으로 설정
		inputPanel.setBackground(new Color(250, 240, 230)); // 배경색 통일
		chatField = new JTextField();
		chatField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		chatField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		chatField.setMargin(new Insets(0, 0, 0, 0)); // 텍스트 필드 내부 여백 제거
		inputPanel.add(chatField, BorderLayout.CENTER);

		sendButton = new JButton("전송");
		sendButton.setPreferredSize(new Dimension(80, 40));
		styleButton(sendButton);
		inputPanel.add(sendButton, BorderLayout.EAST);

		// 팔레트 버튼 설정
		paletteButton = new JButton();
		paletteButton.setPreferredSize(new Dimension(44, 40)); // 크기를 이미지에 맞게 설정

		// 팔레트 이미지 추가
		ImageIcon paletteIcon = new ImageIcon(getClass().getResource("/Image/팔레트1.png"));
		Image scaledPaletteImage = paletteIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		paletteButton.setIcon(new ImageIcon(scaledPaletteImage)); // 버튼에 이미지 아이콘 추가

		// 버튼 스타일 제거
		paletteButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // 검은색 테두리 추가
		paletteButton.setContentAreaFilled(false); // 배경 제거
		paletteButton.setFocusPainted(false); // 포커스 테두리 제거
		paletteButton.setOpaque(false); // 투명하게 설정

		// 버튼 클릭 시 효과를 위해 손 모양 커서 추가
		paletteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// 마우스 호버 효과 추가
		paletteButton.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        // 마우스가 버튼 위에 올라갔을 때
		        paletteButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // 테두리를 회색으로 변경
		        paletteButton.setContentAreaFilled(true); // 배경 활성화
		        paletteButton.setBackground(new Color(220, 220, 220)); // 연한 회색 배경색 추가
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        // 마우스가 버튼에서 나갔을 때
		        paletteButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // 원래 검은 테두리로 복원
		        paletteButton.setContentAreaFilled(false); // 배경 제거
		    }
		});

		// 팔레트 버튼 클릭 이벤트 추가
		paletteButton.addActionListener(e -> openPaletteDialog());

		// 버튼을 입력 영역에 추가
		inputPanel.add(paletteButton, BorderLayout.WEST);

		mainPanel.add(inputPanel, BorderLayout.SOUTH);

		// 메시지 전송 이벤트
		sendButton.addActionListener(e -> sendMessage());
		chatField.addActionListener(e -> sendMessage());


		setVisible(true);
	}

	private void sendMessage() {
		String message = chatField.getText().trim();
		if (!message.isEmpty()) {
			// 서버로 메시지 전송
			clientBack.sendMessage(new MessageDTO(MessageType.PrivateChat, sender + ":" + recipient + ":" + message));
			chatField.setText(""); // 입력창 초기화
		}
	}

	public void appendMessage(String message, boolean isSender) {
		StyledDocument doc = chatPane.getStyledDocument();

		try {
			JPanel messagePanel = new JPanel();
			messagePanel.setLayout(new FlowLayout(isSender ? FlowLayout.RIGHT : FlowLayout.LEFT));
			messagePanel.setBackground(new Color(240, 240, 240)); // 기본 배경색 (채팅창과 동일)

			JLabel messageLabel = new JLabel(message);
			messageLabel.setOpaque(true);
			messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			messageLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
			messageLabel.setForeground(textColor); // 사용자 지정 텍스트 색상

			if (isSender) {
				messageLabel.setBackground(new Color(187, 222, 251)); // 파란색
				messageLabel.setBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 181, 246), 2),
								BorderFactory.createEmptyBorder(5, 10, 5, 10)));
			} else {
				messageLabel.setBackground(new Color(255, 235, 59)); // 노란색
				messageLabel.setBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 193, 7), 2),
								BorderFactory.createEmptyBorder(5, 10, 5, 10)));

			}

			messagePanel.add(messageLabel);

			SimpleAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setComponent(attr, messagePanel);
			doc.insertString(doc.getLength(), "\n", attr); // 새 메시지 추가
			chatPane.setCaretPosition(doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void openPaletteDialog() {
	    // 색상 선택 옵션: 텍스트 색상과 배경 색상을 선택하도록 구분
	    Object[] options = {"글자 색상", "배경 색상", "취소"};
	    int choice = JOptionPane.showOptionDialog(
	        this,
	        "변경할 항목을 선택하세요:",
	        "팔레트 옵션",
	        JOptionPane.DEFAULT_OPTION,
	        JOptionPane.PLAIN_MESSAGE,
	        null,
	        options,
	        options[0]
	    );

	    if (choice == 0) { // 글자 색상 선택
	        Color chosenTextColor = JColorChooser.showDialog(this, "글자 색상 선택", textColor);
	        if (chosenTextColor != null) {
	            textColor = chosenTextColor;
	        }
	    } else if (choice == 1) { // 배경 색상 선택
	        Color chosenBackgroundColor = JColorChooser.showDialog(this, "채팅 배경 색상 선택", chatPane.getBackground());
	        if (chosenBackgroundColor != null) {
	            chatPane.setBackground(chosenBackgroundColor); // 채팅 배경색 변경
	        }
	    }
	}

	private void styleButton(JButton button) {
		button.setFocusPainted(false);
		button.setBackground(new Color(200, 200, 200));
		button.setForeground(Color.BLACK);
		button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		button.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(new Color(220, 220, 220));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(new Color(200, 200, 200));
			}
		});
	}
}