package Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PasswordStrengthChecker extends JFrame {
    private JPasswordField passwordField;
    private JProgressBar progressBar;
    private JLabel strengthLabel;

    public PasswordStrengthChecker() {
        setTitle("Password Strength Checker");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        passwordField = new JPasswordField(20);
        progressBar = new JProgressBar(0, 100);  // 0 ~ 100 범위 설정
        progressBar.setStringPainted(true);  // 프로그레스바에 글자 표시
        strengthLabel = new JLabel("Strength: ");

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String password = new String(passwordField.getPassword());
                int strength = calculatePasswordStrength(password);  // 비밀번호 강도 계산

                // 프로그레스바 업데이트
                progressBar.setValue(strength);

                // 강도에 따라 레이블 및 프로그레스바 색상 업데이트
                if (strength <= 25) {
                    progressBar.setForeground(Color.RED);  // 빨간색
                    strengthLabel.setText("위험");
                    strengthLabel.setForeground(Color.RED);  // 라벨 색상도 빨간색
                } else if (strength <= 50) {
                    progressBar.setForeground(Color.ORANGE);  // 주황색
                    strengthLabel.setText("보통");
                    strengthLabel.setForeground(Color.ORANGE);  // 라벨 색상도 주황색
                } else if (strength <= 75) {
                    progressBar.setForeground(Color.GREEN);  // 초록색
                    strengthLabel.setText("안전");
                    strengthLabel.setForeground(Color.GREEN);  // 라벨 색상도 초록색
                } else {
                    progressBar.setForeground(Color.GREEN);  // 매우 안전도 초록색
                    strengthLabel.setText("매우 안전");
                    strengthLabel.setForeground(Color.GREEN);  // 라벨 색상도 초록색
                }
            }
        });

        add(new JLabel("Password: "));
        add(passwordField);
        add(progressBar);
        add(strengthLabel);

        setVisible(true);
    }

    private int calculatePasswordStrength(String password) {
        // 비밀번호 강도를 계산하는 로직
        int strength = 0;
        
        // 길이가 8 이상인 경우
        if (password.length() >= 8) strength += 25;
        
        // 숫자가 포함된 경우
        if (password.matches(".*\\d.*")) strength += 25;
        
        // 소문자가 포함된 경우
        if (password.matches(".*[a-z].*")) strength += 25;
        
        // 대문자나 특수문자가 포함된 경우
        if (password.matches(".*[A-Z!@#$%^&*()].*")) strength += 25;

        return strength;
    }

    public static void main(String[] args) {
        new PasswordStrengthChecker();
    }
}