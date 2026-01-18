package gameClient.Main;

import DB.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdminLoginFrame extends JFrame {

    private Database db = new Database();
    private JTextField adminIdField;
    private JPasswordField adminPwField;

    public AdminLoginFrame() {
        setTitle("관리자 로그인");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        JPanel panel = new JPanel(); 
        panel.setLayout(null); 
        setContentPane(panel);
        
        JLabel idLabel = new JLabel("Admin ID:"); 
        idLabel.setBounds(50, 70, 80, 25); 
        panel.add(idLabel); 
        
        adminIdField = new JTextField();
        adminIdField.setBounds(150, 70, 150, 25); 
        panel.add(adminIdField); 
        
        JLabel pwLabel = new JLabel("Password:"); 
        pwLabel.setBounds(50, 110, 80, 25); 
        panel.add(pwLabel); 
        
        adminPwField = new JPasswordField(); 
        adminPwField.setBounds(150, 110, 150, 25); 
        panel.add(adminPwField); 
        
        JButton loginButton = new JButton("Login"); 
        loginButton.setBounds(150, 160, 80, 25); 
        panel.add(loginButton);
        
        // 로그인 버튼 클릭 시 로그인 처리
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Enter 키를 눌렀을 때 로그인 처리
        adminIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        adminPwField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        setVisible(true);
    }

    private void performLogin() {
        String adminId = adminIdField.getText();
        String adminPassword = new String(adminPwField.getPassword());
        if (db.adminLoginCheck(adminId, adminPassword)) {
            AdminFrame adminFrame = new AdminFrame();
            adminFrame.setVisible(true);
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(null, "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.", "로그인 오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
