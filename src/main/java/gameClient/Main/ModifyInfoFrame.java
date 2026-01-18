package gameClient.Main;

import DB.Database;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ModifyInfoFrame extends JFrame {
    private Database db = new Database();
    private JTextField idField, nameField, nickNameField, emailField, phoneField, addressField, birthdateField;
    private JPasswordField passwordField; // 비밀번호 필드
    private JRadioButton maleRadioBtn, femaleRadioBtn;
    private boolean passwordVisible = false; // 비밀번호 표시 여부
    private JButton togglePasswordBtn; // 비밀번호 보기 버튼

    public ModifyInfoFrame(String userId) {
        setTitle("회원 정보 수정");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 550); // 높이 늘리기
        setLayout(null);

        // ID 필드 (읽기 전용)
        idField = new JTextField(userId);
        idField.setEditable(false);
        idField.setBounds(150, 20, 200, 30);
        add(new JLabel("회원 ID")).setBounds(30, 20, 100, 30);
        add(idField);

        // 비밀번호 패널 추가
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordField = new JPasswordField(); 
        passwordField.setPreferredSize(new Dimension(150, 30)); // 크기 설정

        passwordPanel.add(passwordField, BorderLayout.CENTER);

        // 비밀번호 보기 버튼 추가
        togglePasswordBtn = new JButton("👁"); // 눈 모양 이모지 사용
        togglePasswordBtn.setPreferredSize(new Dimension(40, 30));
        togglePasswordBtn.setFocusPainted(false); // 버튼 테두리 제거
        togglePasswordBtn.setContentAreaFilled(false); // 버튼 배경 제거
        togglePasswordBtn.setBorder(null); // 버튼 외곽선 제거
        togglePasswordBtn.addActionListener(e -> togglePasswordVisibility());
        passwordPanel.add(togglePasswordBtn, BorderLayout.EAST);

        add(new JLabel("비밀번호:")).setBounds(30, 70, 100, 30);
        passwordPanel.setBounds(150, 70, 200, 30);
        add(passwordPanel);

        // 다른 정보 입력 필드
        nameField = new JTextField();
        nickNameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        addressField = new JTextField();
        birthdateField = new JTextField();

        add(new JLabel("이름:")).setBounds(30, 120, 100, 30);
        nameField.setBounds(150, 120, 200, 30);
        add(nameField);

        add(new JLabel("닉네임:")).setBounds(30, 170, 100, 30);
        nickNameField.setBounds(150, 170, 200, 30);
        add(nickNameField);

        add(new JLabel("이메일:")).setBounds(30, 220, 100, 30);
        emailField.setBounds(150, 220, 200, 30);
        add(emailField);

        add(new JLabel("전화번호:")).setBounds(30, 270, 100, 30);
        phoneField.setBounds(150, 270, 200, 30);
        add(phoneField);

        add(new JLabel("주소:")).setBounds(30, 320, 100, 30);
        addressField.setBounds(150, 320, 200, 30);
        add(addressField);

        add(new JLabel("생년월일:")).setBounds(30, 370, 100, 30);
        birthdateField.setBounds(150, 370, 200, 30);
        add(birthdateField);

        // 성별 선택 라디오 버튼
        maleRadioBtn = new JRadioButton("남자");
        femaleRadioBtn = new JRadioButton("여자");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioBtn);
        genderGroup.add(femaleRadioBtn);

        JPanel genderPanel = new JPanel();
        genderPanel.add(maleRadioBtn);
        genderPanel.add(femaleRadioBtn);
        genderPanel.setBounds(150, 420, 200, 30);
        add(new JLabel("성별:")).setBounds(30, 420, 100, 30);
        add(genderPanel);

        // 수정 버튼
        JButton updateBtn = new JButton("수정하기");
        updateBtn.setBounds(150, 470, 100, 30);
        updateBtn.addActionListener(e -> updateUserInfo(userId));
        add(updateBtn);

        // 나가기 버튼
        JButton exitBtn = new JButton("나가기");
        exitBtn.setBounds(270, 470, 100, 30);
        exitBtn.addActionListener(e -> dispose()); // 창 닫기
        add(exitBtn);

        // 회원 정보 로드
        loadUserInfo(userId);

        setVisible(true);
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordField.setEchoChar('●'); // 비밀번호 숨기기
            togglePasswordBtn.setText("👁"); // 눈 모양으로 변경
        } else {
            passwordField.setEchoChar((char) 0); // 비밀번호 표시
            togglePasswordBtn.setText("❌"); // 숨기기 모양으로 변경 (원하는 다른 이모지로 대체 가능)
        }
        passwordVisible = !passwordVisible;
    }

    private void loadUserInfo(String userId) {
        try {
            String query = "SELECT password, name, nickName, email, phone, address, birthdate, gender FROM users WHERE id = ?";
            PreparedStatement pstmt = db.getConnection().prepareStatement(query);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                passwordField.setText(rs.getString("password"));
                nameField.setText(rs.getString("name"));
                nickNameField.setText(rs.getString("nickName"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                addressField.setText(rs.getString("address"));
                birthdateField.setText(rs.getString("birthdate"));
                String gender = rs.getString("gender");
                maleRadioBtn.setSelected("M".equals(gender));
                femaleRadioBtn.setSelected("F".equals(gender));
            } else {
                JOptionPane.showMessageDialog(this, "회원 정보를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "회원 정보 로드 실패", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUserInfo(String userId) {
        String name = nameField.getText();
        String nickName = nickNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String birthdate = birthdateField.getText();
        String gender = maleRadioBtn.isSelected() ? "M" : "F";
        String password = new String(passwordField.getPassword()); // 비밀번호 가져오기

        try {
            // 현재 데이터베이스에서 기존 정보 조회
            String queryCheck = "SELECT name, password, nickName, email, phone, address, birthdate, gender FROM users WHERE id = ?";
            PreparedStatement pstmtCheck = db.getConnection().prepareStatement(queryCheck);
            pstmtCheck.setString(1, userId);
            ResultSet rsCheck = pstmtCheck.executeQuery();

            if (rsCheck.next()) {
                // 기존 정보와 비교
                boolean isModified = !name.equals(rsCheck.getString("name"))
                        || !nickName.equals(rsCheck.getString("nickName")) || !email.equals(rsCheck.getString("email"))
                        || !phone.equals(rsCheck.getString("phone")) || !address.equals(rsCheck.getString("address"))
                        || !birthdate.equals(rsCheck.getString("birthdate"))
                        || !gender.equals(rsCheck.getString("gender"))
                        || !password.equals(rsCheck.getString("password")); // 비밀번호 수정 여부 체크

                if (!isModified) {
                    JOptionPane.showMessageDialog(this, "수정된 사항이 없습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                    return; // 수정되지 않은 경우 메서드 종료
                }
            }

            // 수정 쿼리 실행
            String query = "UPDATE users SET name = ?, nickName = ?, email = ?, phone = ?, address = ?, birthdate = ?, gender = ?, password = ? WHERE id = ?";
            PreparedStatement pstmt = db.getConnection().prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, nickName);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, birthdate);
            pstmt.setString(7, gender);
            pstmt.setString(8, password); // 비밀번호 업데이트
            pstmt.setString(9, userId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "회원 정보가 수정되었습니다.");
                dispose(); // 수정 후 창 닫기
            } else {
				JOptionPane.showMessageDialog(this, "회원 정보 수정 실패", "오류", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "회원 정보 수정 실패", "오류", JOptionPane.ERROR_MESSAGE);
		}
	}
}