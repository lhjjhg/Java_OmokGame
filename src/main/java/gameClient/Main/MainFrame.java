package gameClient.Main;

import DB.Database;
import gameClient.GameLobby.CharacterSelectFrame;
import gameClient.GameLobby.ChatLayout;
import gameClient.Signup.JoinFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Database db = new Database();
	private Statement stmt = db.stmt;
	private StringBuilder sb;
	private String sql;
	private ResultSet srs = null;
	private int portNum;

	private JPanel mainPanel;

	/* TextField */
	private JTextField idT;
	private JPasswordField pwT;

	/* Label */
	private JLabel mainL;
	private JLabel idL;
	private JLabel pwL;

	/* Button */
	private JButton registerBtn;
	private JButton loginBtn;
	private JButton exitBtn;
	private JButton weatherBtn;
//	private JButton adminBtn;

	private JPanel inputPanel;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public MainFrame(int portNum) {

		this.portNum = portNum;

		setTitle("오목게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 735, 575);

		mainPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon backgroundImage = new ImageIcon("C:\\Users\\lhjjh\\Desktop\\image\\오목 배경화면.png");
				Image img = backgroundImage.getImage();
				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			}
		};
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(null);
		setContentPane(mainPanel);

		mainL = new JLabel("Omok Game");
		mainL.setForeground(new Color(255, 255, 255));
		mainL.setFont(new Font("Yu Gothic", Font.BOLD | Font.ITALIC, 50));
		mainL.setBounds(211, 95, 327, 175);
		mainPanel.add(mainL);

		idL = new JLabel("ID");
		idL.setForeground(new Color(255, 255, 255));
		idL.setBounds(444, 364, 22, 15);
		idL.setFont(new Font("나눔고딕", Font.BOLD, 15));
		mainPanel.add(idL);

		idT = new JTextField();
		idT.setBounds(476, 358, 148, 30);
		idT.setColumns(10);
		mainPanel.add(idT);

		pwT = new JPasswordField();
		pwT.setBounds(476, 398, 148, 30);
		mainPanel.add(pwT);

		pwL = new JLabel("Password");
		pwL.setForeground(new Color(255, 255, 255));
		pwL.setBounds(393, 404, 73, 15);
		pwL.setFont(new Font("나눔고딕", Font.BOLD, 15));
		mainPanel.add(pwL);

		registerBtn = new JButton("회원가입");
		registerBtn.setBackground(Color.LIGHT_GRAY);
		registerBtn.setForeground(new Color(255, 255, 255));
		registerBtn.setBounds(415, 485, 135, 30);
		mainPanel.add(registerBtn);

		exitBtn = new JButton("종료 ");
		exitBtn.setBackground(Color.LIGHT_GRAY);
		exitBtn.setForeground(new Color(255, 255, 255));
		exitBtn.setBounds(578, 485, 135, 30);
		mainPanel.add(exitBtn);

//		weatherBtn = new JButton("날씨 조회");
//		weatherBtn.setBackground(Color.LIGHT_GRAY);
//		weatherBtn.setForeground(new Color(255, 255, 255));
//		weatherBtn.setBounds(10, 485, 127, 30);
//		mainPanel.add(weatherBtn);

		loginBtn = new JButton("로그인");
		loginBtn.setForeground(new Color(255, 255, 255));
		loginBtn.setBackground(Color.LIGHT_GRAY);
		loginBtn.setBounds(630, 358, 83, 73);
		mainPanel.add(loginBtn);

//		// 관리자 버튼을 추가
//		adminBtn = new JButton("관리자");
//		adminBtn.setBackground(Color.LIGHT_GRAY);
//		adminBtn.setForeground(new Color(255, 255, 255));
//		adminBtn.setBounds(586, 10, 127, 30);
//		mainPanel.add(adminBtn);

		JButton IdCheckBtn = new JButton("아이디 찾기");
		IdCheckBtn.setForeground(Color.WHITE);
		IdCheckBtn.setBackground(Color.LIGHT_GRAY);
		IdCheckBtn.setBounds(415, 449, 135, 30);
		mainPanel.add(IdCheckBtn);

		JButton registerBtn_2 = new JButton("비밀번호 찾기");
		registerBtn_2.setForeground(Color.WHITE);
		registerBtn_2.setBackground(Color.LIGHT_GRAY);
		registerBtn_2.setBounds(578, 449, 135, 30);
		mainPanel.add(registerBtn_2);

		/* Button 이벤트 리스너 */
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginAction();
			}
		});

		registerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				joinAction();
			}
		});

		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitAction();
			}
		});

//		weatherBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				weatherAction();
//			}
//		});

//		// 관리자 버튼 클릭 시 이벤트 리스너
//		adminBtn.addActionListener(new ActionListener() {
//		    @Override
//		    public void actionPerformed(ActionEvent e) {
//		        adminAction();
//		    }
//		});

		IdCheckBtn.addActionListener(e -> new FindIdFrame());

		registerBtn_2.addActionListener(e -> new FindPasswordFrame());

		setVisible(true);
	}

	/* 로그인 처리 메서드 */
	private void loginAction() {
		String uid = idT.getText();
		String upass = new String(pwT.getPassword());

		if (uid.equals("") || upass.equals("")) {
			JOptionPane.showMessageDialog(null, "아이디와 비밀번호 모두 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
			System.out.println("로그인 실패 > 로그인 정보 미입력");
		} else {

			if (db.adminLoginCheck(uid, upass)) {
				AdminFrame adminFrame = new AdminFrame(); // 관리자 화면 열기
				adminFrame.setVisible(true);
				dispose(); // MainFrame 닫기
				JOptionPane.showMessageDialog(null, "관리자 계정으로 로그인 성공");
			}
			else if (db.logincheck(uid, upass)) {
				String nickName = db.getNickName(uid);
				String character = db.getCharacter(nickName); // 캐릭터 정보 조회

				if (character == null || character.isEmpty()) {
					// 캐릭터 설정이 없는 경우 CharacterSelectFrame으로 이동
					new CharacterSelectFrame(nickName, "localhost", portNum);
				} else {
					// 캐릭터 설정이 있는 경우 ChatLayout으로 이동
					ChatLayout chatLayout = new ChatLayout(nickName, "localhost", portNum);
					chatLayout.setCharacterImage(character); // 기존 캐릭터 이미지 설정
					chatLayout.setVisible(true);
				}
				dispose(); // MainFrame 닫기
				JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다");
			} else {
				System.out.println("로그인 실패 > 로그인 정보 불일치");
				JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다");
			}
		}
	}

	/* 회원가입 처리 메서드 */
	private void joinAction() {
		JoinFrame jf = new JoinFrame();
		jf.setVisible(true);
		System.out.println("회원가입 창이 열렸습니다.");
	}

	/* 프로그램 종료 처리 메서드 */
	private void exitAction() {
		System.out.println("프로그램 종료");
		System.exit(0);
	}

	/* 날씨 조회 처리 메서드 */
	private void weatherAction() {
		WeatherFrame w = new WeatherFrame();
		w.setVisible(true);
	}

//	private void adminAction() {
//	    AdminLoginFrame adminFrame = new AdminLoginFrame();
//	    adminFrame.setVisible(true);
//	}
}
