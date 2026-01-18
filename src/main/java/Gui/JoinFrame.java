package Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class JoinFrame extends JFrame {

	/* Panel */
	private JPanel contentPane;
	private JPanel mainPanel;

	/* TextField */
	private JTextField idT;
	private JPasswordField pwT;
	private JPasswordField confirmPwT;
	private JTextField nameT;
	private JTextField nicknameT;
	private JTextField phoneT_2;
	private JTextField phoneT_3;
	private JTextField emailT_1;
	private JTextField emailT_2;
	private JTextField addrT_1;
	private JTextField addrT_2;
	

	private byte[] imageBytes;
	private byte[] profileImageBytes;

	/* Label */
	private JLabel profileImageL;
	private JLabel registerL;
	private JLabel idL;
	private JLabel pwL;
	private JLabel confirmpwL;
	private JLabel nameL;
	private JLabel nickNameL;
	private JLabel phoneL;
	private JLabel phoneDashL_1;
	private JLabel phoneDashL_2;
	private JLabel emailL;
	private JLabel emailSignL;
	private JLabel addrL;
	private JLabel birthdayL;
	private JLabel yearL;
	private JLabel monthL;
	private JLabel dayL;
	private JLabel sexL;
	private JLabel strengthLabel;

	/* Button */
	private JRadioButton maleRadioBtn;
	private JRadioButton femaleRadioBtn;

	/* ComboBox */
	private JComboBox<String> phone_1;
	private JComboBox<String> emailBox;
	private JComboBox<String> yearBox;
	private JComboBox<String> monthBox;
	private JComboBox<String> dayBox;

	private JProgressBar passwordStrengthBar;

	Operator o = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Operator opt = new Operator(); // Operator 객체 생성
					JoinFrame frame = new JoinFrame(opt);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JoinFrame(Operator _o) {

		o = _o;

		setTitle("signup");
		/* panel */
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		setBackground(new Color(255, 255, 255));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 531, 766);

		mainPanel = new JPanel();
		mainPanel.setBackground(SystemColor.control);
		mainPanel.setBounds(0, 0, 625, 694);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);

		// Label
		registerL = new JLabel("회원가입");
		registerL.setForeground(SystemColor.desktop);
		registerL.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		registerL.setBounds(23, 10, 96, 51);
		mainPanel.add(registerL);

		idL = new JLabel("아이디");
		idL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		idL.setBounds(33, 105, 50, 15);
		mainPanel.add(idL);

		pwL = new JLabel("비밀번호");
		pwL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		pwL.setBounds(23, 166, 50, 15);
		mainPanel.add(pwL);

		confirmpwL = new JLabel("비밀번호 확인");
		confirmpwL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		confirmpwL.setBounds(10, 210, 73, 15);
		mainPanel.add(confirmpwL);

		nameL = new JLabel("이름");
		nameL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		nameL.setBounds(33, 259, 50, 15);
		mainPanel.add(nameL);
		
		nickNameL = new JLabel("닉네임");
		nickNameL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		nickNameL.setBounds(23, 298, 50, 15);
		mainPanel.add(nickNameL);

		phoneL = new JLabel("전화번호");
		phoneL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		phoneL.setBounds(23, 343, 50, 15);
		mainPanel.add(phoneL);

		phoneDashL_1 = new JLabel("-");
		phoneDashL_1.setFont(new Font("나눔고딕", Font.PLAIN, 16));
		phoneDashL_1.setBounds(143, 341, 18, 15);
		mainPanel.add(phoneDashL_1);

		phoneDashL_2 = new JLabel("-");
		phoneDashL_2.setFont(new Font("나눔고딕", Font.PLAIN, 16));
		phoneDashL_2.setBounds(236, 341, 18, 15);
		mainPanel.add(phoneDashL_2);

		emailL = new JLabel("이메일");
		emailL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		emailL.setBounds(23, 392, 50, 15);
		mainPanel.add(emailL);

		emailSignL = new JLabel("@");
		emailSignL.setFont(new Font("나눔고딕", Font.PLAIN, 15));
		emailSignL.setBounds(200, 398, 18, 15);
		mainPanel.add(emailSignL);

		addrL = new JLabel("주소");
		addrL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		addrL.setBounds(23, 449, 50, 15);
		mainPanel.add(addrL);

		birthdayL = new JLabel("생년월일");
		birthdayL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		birthdayL.setBounds(23, 541, 50, 15);
		mainPanel.add(birthdayL);

		yearL = new JLabel("년");
		yearL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		yearL.setBounds(154, 541, 18, 15);
		mainPanel.add(yearL);

		monthL = new JLabel("월");
		monthL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		monthL.setBounds(236, 541, 18, 15);
		mainPanel.add(monthL);

		dayL = new JLabel("일");
		dayL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		dayL.setBounds(314, 541, 18, 15);
		mainPanel.add(dayL);

		sexL = new JLabel("성별");
		sexL.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		sexL.setBounds(23, 588, 50, 15);
		mainPanel.add(sexL);

		profileImageL = new JLabel("프로필 이미지");
		profileImageL.setHorizontalAlignment(SwingConstants.CENTER);
		profileImageL.setBounds(451, 98, 150, 150);
		profileImageL.setOpaque(true);
		profileImageL.setBackground(Color.LIGHT_GRAY);
		mainPanel.add(profileImageL);

		// TextField
		idT = new JTextField();
		idT.setBounds(93, 98, 161, 29);
		mainPanel.add(idT);
		idT.setColumns(10);

		pwT = new JPasswordField(20);
		pwT.setBounds(93, 159, 188, 29);
		mainPanel.add(pwT);

		confirmPwT = new JPasswordField();
		confirmPwT.setBounds(93, 203, 245, 29);
		mainPanel.add(confirmPwT);

		nameT = new JTextField();
		nameT.setColumns(10);
		nameT.setBounds(93, 252, 188, 29);
		mainPanel.add(nameT);
		
		nicknameT = new JTextField();
		nicknameT.setBounds(93, 291, 161, 29);
		mainPanel.add(nicknameT);
		nicknameT.setColumns(10);

		phoneT_2 = new JTextField();
		phoneT_2.setColumns(10);
		phoneT_2.setBounds(164, 336, 62, 29);
		mainPanel.add(phoneT_2);

		phoneT_3 = new JTextField();
		phoneT_3.setColumns(10);
		phoneT_3.setBounds(258, 336, 62, 29);
		mainPanel.add(phoneT_3);

		emailT_1 = new JTextField();
		emailT_1.setColumns(10);
		emailT_1.setBounds(83, 392, 107, 29);
		mainPanel.add(emailT_1);

		emailT_2 = new JTextField();
		emailT_2.setColumns(10);
		emailT_2.setBounds(228, 392, 107, 29);
		mainPanel.add(emailT_2);

		addrT_1 = new JTextField();
		addrT_1.setColumns(10);
		addrT_1.setBounds(83, 442, 107, 29);
		mainPanel.add(addrT_1);

		addrT_2 = new JTextField();
		addrT_2.setColumns(10);
		addrT_2.setBounds(83, 481, 267, 29);
		mainPanel.add(addrT_2);

		// JButton
		JButton idCheckBtn = new JButton("증복확인");
		idCheckBtn.setFont(new Font("굴림", Font.PLAIN, 11));
		idCheckBtn.setBounds(263, 99, 107, 29);
		mainPanel.add(idCheckBtn);
		
		JButton nicknameCheckBtn = new JButton("증복확인");
		nicknameCheckBtn.setFont(new Font("굴림", Font.PLAIN, 11));
		nicknameCheckBtn.setBounds(264, 292, 107, 29);
		mainPanel.add(nicknameCheckBtn);

		JButton addrBtn = new JButton("우편번호 검색");
		addrBtn.setBounds(200, 441, 114, 30);
		mainPanel.add(addrBtn);

		JButton imageBtn = new JButton("이미지 업로드");
		imageBtn.setBounds(451, 258, 150, 30);
		mainPanel.add(imageBtn);

		JButton joinBtn = new JButton("가입하기");
		joinBtn.setForeground(Color.BLACK);
		joinBtn.setBackground(SystemColor.inactiveCaption);
		joinBtn.setBounds(38, 635, 200, 40);
		mainPanel.add(joinBtn);

		JButton cancelBtn = new JButton("가입취소");
		cancelBtn.setForeground(Color.BLACK);
		cancelBtn.setBackground(SystemColor.inactiveCaption);
		cancelBtn.setBounds(370, 635, 200, 40);
		mainPanel.add(cancelBtn);

		// JCombobox
		phone_1 = new JComboBox<>(); // 초기화
		phone_1.setModel(new DefaultComboBoxModel(
				new String[] { "010", "011", "02", "031", "032", "033", "041", "042", "043" }));
		phone_1.setBounds(83, 335, 50, 30);
		mainPanel.add(phone_1);

		emailBox = new JComboBox<>();
		emailBox.setModel(
				new DefaultComboBoxModel(new String[] { "naver.com", "gmail.com", "daum.com", "anyang.ac.kr" }));
		emailBox.setBounds(349, 392, 89, 29);
		mainPanel.add(emailBox);

		yearBox = new JComboBox<>();
		yearBox.setModel(new DefaultComboBoxModel(new String[] { "1970", "1971", "1972", "1973", "1974", "1975", "1976",
				"1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989",
				"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002",
				"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015",
				"2016", "2017", "2018", "2019", "2022", "2023", "2024" }));
		yearBox.setBounds(83, 534, 62, 29);
		mainPanel.add(yearBox);

		monthBox = new JComboBox<>();
		monthBox.setModel(new DefaultComboBoxModel(
				new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
		monthBox.setBounds(182, 534, 40, 29);
		mainPanel.add(monthBox);

		dayBox = new JComboBox<>();
		dayBox.setModel(new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
				"27", "28", "29", "30", "31" }));
		dayBox.setBounds(264, 534, 40, 29);
		mainPanel.add(dayBox);

		// 성별 라디오 버튼
		maleRadioBtn = new JRadioButton("남");
		maleRadioBtn.setBounds(69, 584, 50, 23);
		mainPanel.add(maleRadioBtn);

		femaleRadioBtn = new JRadioButton("여");
		femaleRadioBtn.setBounds(122, 584, 50, 23);
		mainPanel.add(femaleRadioBtn);

		// 비밀번호 프로그래스 바 (강도체크)
		passwordStrengthBar = new JProgressBar(0, 100);
		passwordStrengthBar.setValue(0); // 초기값 설정
		passwordStrengthBar.setStringPainted(true); // 텍스트 표시
		passwordStrengthBar.setBounds(291, 159, 107, 29);
		mainPanel.add(passwordStrengthBar);

		/* Button 이벤트 리스너 추가 */
		ButtonListener bl = new ButtonListener();

		cancelBtn.addActionListener(bl);
		joinBtn.addActionListener(bl);

		setSize(627, 724);
		setLocationRelativeTo(null);
		setResizable(false);

		/* addActionListener */

		// 이메일 도메인 선택 액션 리스너
		emailBox.addActionListener(e -> {
			String selectedDomain = (String) emailBox.getSelectedItem();
			emailT_2.setText(selectedDomain);
			
			
		});

		// 아이디 중복 버튼 이벤트
		idCheckBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idT.getText().trim();
				if (id.isEmpty()) {
					JOptionPane.showMessageDialog(null, "아이디를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
				} else {
					boolean isDuplicate = o.db.checkIdDuplicate(id);
					if (isDuplicate) {
						JOptionPane.showMessageDialog(null, "중복된 아이디입니다.", "경고", JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.", "확인", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		
		// 닉네임 중복 버튼 이벤트 
		nicknameCheckBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nickname = nicknameT.getText().trim();
				if (nickname.isEmpty()) {
					JOptionPane.showMessageDialog(null, "닉네임을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
				} else {
					boolean isDuplicate = o.db.checkIdDuplicate(nickname);
					if (isDuplicate) {
						JOptionPane.showMessageDialog(null, "중복된 닉네임입니다.", "경고", JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "사용 가능한 닉네임입니다.", "확인", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		// 이미지 버튼 이벤트
		imageBtn.addActionListener(e -> uploadPhoto());

		// 우편번호 검색 버튼
		addrBtn.addActionListener(e -> openPostalCodeWindow());

		// 비번 입력키 리스너 - 비밀번호 안전도
		pwT.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String password = new String(pwT.getPassword());
				int strength = calculatePasswordStrength(password);
				passwordStrengthBar.setValue(strength);

				// 강도에 따른 비번 안전도 색상 업데이트
				if (strength <= 25) {
					passwordStrengthBar.setForeground(Color.RED);
				} else if (strength <= 50) {
					passwordStrengthBar.setForeground(Color.ORANGE);
				} else if (strength <= 75) {
					passwordStrengthBar.setForeground(Color.GREEN);
				} else {
					passwordStrengthBar.setForeground(Color.GREEN);
				}
			}
		});
		
		
		
		
	}

	// 버튼리스너
	class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();

			/* TextField에 입력된 회원 정보들을 변수에 초기화 */
			String uid = idT.getText();
			String upass = new String(pwT.getPassword());
			String upassConfirm = new String(confirmPwT.getPassword());
			String uname = nameT.getText();
			String unickname = nicknameT.getText();
			String uphone = phone_1.getSelectedItem().toString() + "-" + phoneT_2.getText() + "-" + phoneT_3.getText();
			String uemail = emailT_1.getText() + "@" + emailT_2.getText();
			String uaddress = addrT_2.getText();
			String ubirthdate = yearBox.getSelectedItem().toString() + "년" + monthBox.getSelectedItem().toString() + "월"
					+ dayBox.getSelectedItem().toString() + "일";
			String ugender = maleRadioBtn.isSelected() ? "M" : "F";
			byte[] imageBytes = getImageBytes();

			/* 가입취소 버튼 이벤트 */
			if (b.getText().equals("가입취소")) {
				dispose();
			}

			/* 가입하기 버튼 이벤트 */
			else if (b.getText().equals("가입하기")) {
				if (uid.isEmpty() || upass.isEmpty() || upassConfirm.isEmpty() || uname.isEmpty() || unickname.isEmpty() 
						|| uphone.isEmpty() || uemail.isEmpty() || uaddress.isEmpty() || ubirthdate.isEmpty()) {
					JOptionPane.showMessageDialog(null, "모든 정보를 입력해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("회원가입 실패 > 회원정보 미입력");
				} 
				else if (!isPasswordValid(upass)) {
					JOptionPane.showMessageDialog(null, "비밀번호는 8자 이상 20자 이하이며, 특수 문자를 포함해야 합니다.", "오류",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 
				else if (!upass.equals(upassConfirm)) {
					JOptionPane.showMessageDialog(null, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", "회원가입 실패",
							JOptionPane.ERROR_MESSAGE);
					System.out.println("회원가입 실패 > 비밀번호 불일치");
				} 
				else {
					if (o.db.joinCheck(uid, upass, uname, unickname, uphone, uemail, uaddress, ubirthdate, ugender, imageBytes)) {
						System.out.println("회원가입 완료");
						JOptionPane.showMessageDialog(null, "회원가입에 성공하였습니다");
						dispose();
					} else {
						System.out.println("회원가입 실패");
						JOptionPane.showMessageDialog(null, "회원가입에 실패하였습니다");
						idT.setText("");
						pwT.setText("");
					}
				}
			}
		}
	}

	/* method */
	// 비밀번호 제한
	private boolean isPasswordValid(String password) {
		String regex = "^(?=.*[@#$%^&+=!]).{8,20}$"; // 정규 표현식: 비밀번호는 8~20자, 최소 하나의 특수문자 포함
		return password.matches(regex);
	}

	// PostalCode 오픈 메서드
	private void openPostalCodeWindow() {
		PostalCode postalCodeFrame = new PostalCode(addrT_1, addrT_2);
		postalCodeFrame.setVisible(true);
	}

	// 사진 업로드 메서드
	private void uploadPhoto() {
		JFileChooser fileChooser = new JFileChooser();

		// 이미지 파일 필터 설정
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				String name = f.getName().toLowerCase();
				return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
			}

			@Override
			public String getDescription() {
				return "이미지 파일 (*.jpg, *.jpeg, *.png)";
			}
		});

		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {

				BufferedImage originalImage = ImageIO.read(selectedFile);
				BufferedImage resizedImage = resizeImage(originalImage, 150, 150);
				// 자르기 프레임 생성
				ImageCropFrame cropFrame = new ImageCropFrame(originalImage, this);
				cropFrame.setVisible(true);

				ImageIcon icon = new ImageIcon(resizedImage);
				profileImageL.setIcon(icon);
				profileImageL.setText("");

			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "이미지를 불러오는 데 실패했습니다.", "에러", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// 이미지 크기 조절 메서드
	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH); // 이미지 크기
																												// 조절
		BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB); // 새

		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(resultingImage, 0, 0, null);
		g2d.dispose();
		return outputImage;
	}
	// 자른 이미지 바이트 변환 
	public void setProfileImageBytes(byte[] imageBytes) {
		this.profileImageBytes = imageBytes;
	}
	// 이미지 바이트 배열 변환
	private byte[] getImageBytes() {
		return profileImageBytes;
	}
	// 프로필 이미지 설정 메서드 추가
	public void setProfileImage(BufferedImage image) {
		ImageIcon icon = new ImageIcon(image);
		profileImageL.setIcon(icon);
		profileImageL.setText("");
	}
	// 비밀번호 안전도 검사 메서드(프로그래스바)
	private int calculatePasswordStrength(String password) {
		int strength = 0;
		if (password.length() >= 8)
			strength += 25;
		if (password.length() >= 12)
			strength += 25;
		if (password.matches(".*[!@#$%^&*()].*"))
			strength += 25;
		if (password.matches(".*[0-9].*"))
			strength += 25;
		return strength;
	}
	// JPasswordField와 보기 버튼을 포함한 패널 생성
	private JPanel createPasswordFieldWithButton(JPasswordField passwordField) {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setOpaque(false); // 패널 투명

	    passwordField.setEchoChar('●'); // 기본 비밀번호 숨김 설정
	    panel.add(passwordField, BorderLayout.CENTER);

	    // 보기 버튼 추가
	    JButton viewButton = new JButton("👁");
	    viewButton.setPreferredSize(new Dimension(40, passwordField.getPreferredSize().height));
	    viewButton.setFocusPainted(false); // 버튼 테두리 제거
	    viewButton.setContentAreaFilled(false); // 버튼 배경 제거
	    viewButton.setBorder(null); // 버튼 외곽선 제거
	    viewButton.addActionListener(new ActionListener() {
	        private boolean isPasswordVisible = false; // 기본 숨김 상태

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if (isPasswordVisible) {
	                passwordField.setEchoChar('●'); // 숨김 설정
	                viewButton.setText("👁"); // 보기 아이콘
	            } else {
	                passwordField.setEchoChar((char) 0); // 표시 설정
	                viewButton.setText("❌"); // 숨기기 아이콘
	            }
	            isPasswordVisible = !isPasswordVisible; // 상태 전환
	        }
	    });
	    panel.add(viewButton, BorderLayout.EAST); // 오른쪽에 버튼 추가
	    return panel;
	}
	
}
