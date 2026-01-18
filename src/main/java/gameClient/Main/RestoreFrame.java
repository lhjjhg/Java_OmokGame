package gameClient.Main;

import DB.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestoreFrame extends JFrame {

    private Database db = new Database();
    private JTable deletedUserTable;
    private DefaultTableModel deletedTableModel;

    public RestoreFrame() {
        setTitle("삭제된 회원 복원");
        setBounds(100, 100, 800, 400);
        
        deletedTableModel = new DefaultTableModel(new String[]{"ID", "이름", "닉네임", "이메일", "전화번호", "주소", "생년월일", "성별"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 셀 편집 불가
            }
        };
        
        deletedUserTable = new JTable(deletedTableModel);
        JScrollPane scrollPane = new JScrollPane(deletedUserTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton restoreBtn = new JButton("회원 복원");
        restoreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = deletedUserTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String userId = (String) deletedTableModel.getValueAt(selectedRow, 0);
                    restoreUser(userId);
                } else {
                    JOptionPane.showMessageDialog(null, "복원할 회원을 선택하세요.", "복원 실패", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton exitBtn = new JButton("나가기");
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 창 닫기
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restoreBtn);
        buttonPanel.add(exitBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        loadDeletedUserData();
        setVisible(true);
    }

    // 삭제된 회원 목록 로드
    private void loadDeletedUserData() {
        deletedTableModel.setRowCount(0);
        try {
            String query = "SELECT * FROM deleted_users";
            PreparedStatement pstmt = db.getConnection().prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                deletedTableModel.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("nickName"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("birthdate"),
                    rs.getString("gender")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "삭제된 회원 목록 로드 실패", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 회원 복원
    private void restoreUser(String userId) {
        try {
            // deleted_users 테이블에서 users 테이블로 이동
            String restoreQuery = "INSERT INTO users SELECT * FROM deleted_users WHERE id = ?";
            PreparedStatement restorePstmt = db.getConnection().prepareStatement(restoreQuery);
            restorePstmt.setString(1, userId);
            restorePstmt.executeUpdate();

            // deleted_users 테이블에서 회원 삭제
            String deleteQuery = "DELETE FROM deleted_users WHERE id = ?";
            PreparedStatement deletePstmt = db.getConnection().prepareStatement(deleteQuery);
            deletePstmt.setString(1, userId);
            deletePstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "회원 복원 성공");
            loadDeletedUserData(); // 복원 후 데이터 새로 고침
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "회원 복원 실패", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
