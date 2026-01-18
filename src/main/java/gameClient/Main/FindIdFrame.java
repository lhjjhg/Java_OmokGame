package gameClient.Main;

import DB.Database;

import javax.swing.*;
import java.awt.*;

public class FindIdFrame extends JFrame {

    public FindIdFrame() {
        setTitle("아이디 찾기");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 메인 패널
        JPanel mainPanel = new JPanel(null); // 절대 배치
        mainPanel.setBackground(new Color(250, 240, 230));
        add(mainPanel);

        // 제목 라벨
        JLabel titleLabel = new JLabel("아이디 찾기");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(120, 20, 160, 30); // 위치 및 크기 설정
        mainPanel.add(titleLabel);

        // 이름 라벨 및 필드
        JLabel nameLabel = new JLabel("이름:");
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        nameLabel.setBounds(40, 80, 100, 30);
        mainPanel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(140, 80, 200, 30);
        mainPanel.add(nameField);

        // 이메일 라벨 및 필드
        JLabel emailLabel = new JLabel("이메일:");
        emailLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        emailLabel.setBounds(40, 130, 100, 30);
        mainPanel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(140, 130, 200, 30);
        mainPanel.add(emailField);

        // 찾기 버튼
        JButton findButton = new JButton("찾기");
        findButton.setBounds(140, 190, 120, 40);
        styleButton(findButton);
        mainPanel.add(findButton);

        // 버튼 동작
        findButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (!name.isEmpty() && !email.isEmpty()) {
                Database db = new Database();
                String foundId = db.findIdByNameAndEmail(name, email);

                if (foundId != null) {
                    JOptionPane.showMessageDialog(this, "아이디: " + foundId, "아이디 찾기 성공", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "일치하는 정보가 없습니다.", "아이디 찾기 실패", JOptionPane.ERROR_MESSAGE);
                }

                db.close();
            } else {
                JOptionPane.showMessageDialog(this, "이름과 이메일을 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    // 버튼 스타일
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(200, 200, 200));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
