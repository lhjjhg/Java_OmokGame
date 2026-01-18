package gameClient.Main;

import DB.Database;

import javax.swing.*;
import java.awt.*;

public class FindPasswordFrame extends JFrame {

    public FindPasswordFrame() {
        setTitle("비밀번호 찾기");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 메인 패널
        JPanel mainPanel = new JPanel(null); // 절대 배치
        mainPanel.setBackground(new Color(250, 240, 230));
        add(mainPanel);

        // 제목 라벨
        JLabel titleLabel = new JLabel("비밀번호 찾기");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(100, 20, 200, 30); // 위치 및 크기 설정
        mainPanel.add(titleLabel);

        // 아이디 라벨 및 필드
        JLabel idLabel = new JLabel("아이디:");
        idLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        idLabel.setBounds(40, 80, 100, 30);
        mainPanel.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(140, 80, 200, 30);
        mainPanel.add(idField);

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
            String id = idField.getText().trim();
            String email = emailField.getText().trim();

            if (!id.isEmpty() && !email.isEmpty()) {
                Database db = new Database();
                String foundPassword = db.findPasswordByIdAndEmail(id, email);

                if (foundPassword != null) {
                    JOptionPane.showMessageDialog(this, "비밀번호가 확인되었습니다. 재설정 창으로 이동합니다.", "비밀번호 찾기 성공", JOptionPane.INFORMATION_MESSAGE);
                    new ResetPasswordFrame(id).setVisible(true); // 비밀번호 재설정 창 실행
                    dispose(); // 현재 창 닫기
                } else {
                    JOptionPane.showMessageDialog(this, "일치하는 정보가 없습니다.", "비밀번호 찾기 실패", JOptionPane.ERROR_MESSAGE);
                }

                db.close();
            } else {
                JOptionPane.showMessageDialog(this, "아이디와 이메일을 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
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

// 비밀번호 재설정 창
class ResetPasswordFrame extends JFrame {

    public ResetPasswordFrame(String userId) {
        setTitle("비밀번호 재설정");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 메인 패널
        JPanel mainPanel = new JPanel(null); // 절대 배치
        mainPanel.setBackground(new Color(230, 240, 250));
        add(mainPanel);

        // 제목 라벨
        JLabel titleLabel = new JLabel("비밀번호 재설정");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(100, 20, 200, 30); // 위치 및 크기 설정
        mainPanel.add(titleLabel);

        // 새 비밀번호 라벨 및 필드
        JLabel newPasswordLabel = new JLabel("새 비밀번호:");
        newPasswordLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        newPasswordLabel.setBounds(40, 80, 120, 30);
        mainPanel.add(newPasswordLabel);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setBounds(170, 80, 180, 30);
        mainPanel.add(newPasswordField);

        // 비밀번호 확인 라벨 및 필드
        JLabel confirmPasswordLabel = new JLabel("비밀번호 확인:");
        confirmPasswordLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        confirmPasswordLabel.setBounds(40, 130, 120, 30);
        mainPanel.add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(170, 130, 180, 30);
        mainPanel.add(confirmPasswordField);

        // 재설정 버튼
        JButton resetButton = new JButton("재설정");
        resetButton.setBounds(140, 190, 120, 40);
        styleButton(resetButton);
        mainPanel.add(resetButton);

        // 버튼 동작
        resetButton.addActionListener(e -> {
            String newPassword = new String(newPasswordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            } else if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            } else {
                Database db = new Database();
                if (db.updatePassword(userId, newPassword)) {
                    JOptionPane.showMessageDialog(this, "비밀번호가 성공적으로 변경되었습니다.", "변경 완료", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // 창 닫기
                } else {
                    JOptionPane.showMessageDialog(this, "비밀번호 변경에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
                db.close();
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
