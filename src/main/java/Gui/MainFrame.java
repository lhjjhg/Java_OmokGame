package Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

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

	Operator o = null;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public MainFrame(Operator _o) {
		 o = _o;

		setTitle("MainFrameTest");
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
		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		mainL = new JLabel("Omok Game");
		mainL.setForeground(new Color(255, 255, 255));
		mainL.setFont(new Font("Yu Gothic", Font.BOLD | Font.ITALIC, 50));
		mainL.setBounds(211, 95, 327, 175);
		mainPanel.add(mainL);

		idL = new JLabel("ID");
		idL.setForeground(new Color(255, 255, 255));
		idL.setBounds(444, 378, 22, 15);
		mainPanel.add(idL);
		idL.setFont(new Font("나눔고딕", Font.BOLD, 15));

		idT = new JTextField();
		idT.setBounds(476, 372, 148, 30);
		mainPanel.add(idT);
		idT.setColumns(10);

		pwT = new JPasswordField();
		pwT.setBounds(476, 412, 148, 30);
		mainPanel.add(pwT);

		pwL = new JLabel("Password");
		pwL.setForeground(new Color(255, 255, 255));
		pwL.setBounds(393, 421, 73, 15);
		mainPanel.add(pwL);
		pwL.setFont(new Font("나눔고딕", Font.BOLD, 15));

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

		weatherBtn = new JButton("날씨 조회");
		weatherBtn.setBackground(Color.LIGHT_GRAY);
		weatherBtn.setForeground(new Color(255, 255, 255));
		weatherBtn.setBounds(10, 485, 127, 30);
		mainPanel.add(weatherBtn);

		loginBtn = new JButton("로그인");
		loginBtn.setForeground(new Color(255, 255, 255));
		loginBtn.setBackground(Color.LIGHT_GRAY);
		loginBtn.setBounds(630, 372, 83, 73);
		mainPanel.add(loginBtn);

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

		weatherBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				weatherAction();
			}
		});

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
			if (o.db.logincheck(uid, upass)) {
				System.out.println("로그인 성공");
				dispose();
				JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다");
			} else {
				System.out.println("로그인 실패 > 로그인 정보 불일치");
				JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다");
			}
		}
	}

	/* 회원가입 처리 메서드 */
	private void joinAction() {
		o.jf.setVisible(true);
		System.out.println("회원가입 창이 열렸습니다.");
	}

	/* 프로그램 종료 처리 메서드 */
	private void exitAction() {
		System.out.println("프로그램 종료");
		System.exit(0);
	}

	/* 날씨 조회 처리 메서드 */
	private void weatherAction() {
		WeatherFrame w = new WeatherFrame(o);
		w.setVisible(true);
	}
}
