package gameClient.GameLobby;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import DB.Database;
import java.util.List;

public class MessageSearchFrame extends JFrame {
    private JComboBox<String> roomComboBox;
    private JTextField messageContentField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public MessageSearchFrame(String nickName) {
        setTitle("메시지 검색");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // 상단 검색 패널
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(new Color(240, 240, 240));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setOpaque(false);

        JLabel roomLabel = new JLabel("방 이름:");
        roomComboBox = new JComboBox<>();
        loadRoomNames();

        JLabel messageContentLabel = new JLabel("메시지 내용:");
        messageContentField = new JTextField();

        inputPanel.add(roomLabel);
        inputPanel.add(roomComboBox);
        inputPanel.add(messageContentLabel);
        inputPanel.add(messageContentField);

        JButton searchButton = new JButton("검색");
        searchButton.setFocusPainted(false);
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        searchPanel.add(inputPanel, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // 결과 테이블
        tableModel = new DefaultTableModel(new String[]{"결과"}, 0);
        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(40);
        resultTable.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        resultTable.setShowGrid(false);

        // 테이블 내용 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        resultTable.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("검색 결과"));

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton exitButton = new JButton("나가기");
        exitButton.setFocusPainted(false);
        exitButton.setBackground(new Color(220, 53, 69));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        exitButton.addActionListener(e -> dispose()); // 창 닫기

        buttonPanel.add(exitButton);

        // 메인 레이아웃
        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 데이터베이스에서 방 이름 로드
    private void loadRoomNames() {
        try {
            Database db = new Database();
            List<String> roomNames = db.getRoomList(); // 방 이름 목록 가져오기
            roomComboBox.addItem("방 선택");
            for (String roomName : roomNames) {
                roomComboBox.addItem(roomName);
            }
            db.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "방 이름을 불러오는 중 오류가 발생했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 메시지 검색 실행
    private void performSearch() {
        String roomName = (String) roomComboBox.getSelectedItem();
        String messageContent = messageContentField.getText().trim();

        if (roomName.equals("방 선택") || messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "방 이름과 메시지 내용을 모두 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Database db = new Database();
            List<String[]> results = db.searchMessages(roomName, messageContent);
            tableModel.setRowCount(0); // 기존 결과 초기화

            for (String[] row : results) {
                String formattedMessage = "[" + row[0] + "]: " + row[1] + " (" + row[2] + ")";
                tableModel.addRow(new Object[]{formattedMessage});
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "검색 결과가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            }

            db.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "검색 중 오류가 발생했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
