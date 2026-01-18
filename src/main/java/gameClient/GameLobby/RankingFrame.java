package gameClient.GameLobby;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import DB.Database;

public class RankingFrame extends JFrame {

    public RankingFrame() {
        setTitle("순위표");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 메인 패널 설정
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 240, 230));
        add(mainPanel);

        // 상단 제목
        JLabel titleLabel = new JLabel("순위표", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setForeground(new Color(80, 60, 50)); // 부드러운 갈색
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 순위표 헤더
        String[] columns = {"순위", "닉네임", "승리", "패배", "승률"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable rankingTable = new JTable(model);
        rankingTable.setEnabled(false); // 수정 불가
        rankingTable.getTableHeader().setReorderingAllowed(false); // 컬럼 이동 불가
        rankingTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조정 불가

        // 테이블 스타일링
        rankingTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
        rankingTable.getTableHeader().setBackground(new Color(210, 180, 140)); // 밝은 베이지
        rankingTable.getTableHeader().setForeground(Color.WHITE);
        rankingTable.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        rankingTable.setRowHeight(30);
        rankingTable.setBackground(Color.WHITE);
        rankingTable.setForeground(new Color(80, 60, 50)); // 부드러운 갈색
        rankingTable.setSelectionBackground(new Color(230, 210, 180)); // 선택된 행 배경색
        rankingTable.setSelectionForeground(Color.BLACK);

        // 가운데 정렬 렌더러 설정
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < rankingTable.getColumnCount(); i++) {
            rankingTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // 데이터베이스에서 순위 데이터 가져오기
        try {
            Database db = new Database();
            List<String[]> rankingList = db.getRanking();
            int rank = 1;
            for (String[] row : rankingList) {
                model.addRow(new Object[]{rank++, row[0], row[1], row[2], row[3]});
            }
            db.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "순위 데이터를 불러오는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }

        // 스크롤 패널 추가
        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(250, 240, 230));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton closeButton = new JButton("닫기");
        closeButton.setPreferredSize(new Dimension(120, 40));
        styleButton(closeButton);
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 닫기 버튼 스타일
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(200, 180, 160));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 마우스 오버 효과
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(210, 190, 170)); // 밝아지는 효과
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(200, 180, 160)); // 원래 색상으로 복원
            }
        });
    }
}
