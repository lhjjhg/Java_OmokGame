package gameClient.GameLobby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmoticonSelector extends JDialog {
    private String selectedEmoticonCode; // 선택된 이모티콘 코드
    private final String[] emoticonCodes = {"(행복)", "(슬픔)", "(분노)", "(사랑)", "(놀람)", "(무표정)"};
    private final String[] imagePaths = {
            "/Image/행복.png", "/Image/슬픔.png", "/Image/분노.png",
            "/Image/사랑.png", "/Image/놀람.png", "/Image/무표정.png"
    };

    public EmoticonSelector(JFrame parent) {
        super(parent, "이모티콘 선택", true);

        setSize(300, 300); // 창 크기 축소
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 이모티콘 버튼 패널
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2행 3열 구성
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 이모티콘 버튼 추가
        for (int i = 0; i < emoticonCodes.length; i++) {
            ImageIcon icon = resizeIcon(imagePaths[i], 70, 70); // 이미지 크기 조정
            JButton button = new JButton(icon);
            button.setActionCommand(emoticonCodes[i]); // 코드 설정
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false); // 배경 제거
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // 호버 효과 추가
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(240, 240, 240));
                    button.setOpaque(true);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(Color.WHITE);
                    button.setOpaque(false);
                }
            });

            button.addActionListener(new EmoticonButtonListener());
            buttonPanel.add(button);
        }

        // 스크롤 패널로 감싸기
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    public String getSelectedEmoticonCode() {
        return selectedEmoticonCode;
    }

    private class EmoticonButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedEmoticonCode = e.getActionCommand(); // 선택된 이모티콘 코드 저장
            dispose(); // 창 닫기
        }
    }

    // 이미지 크기 조정 메서드
    private ImageIcon resizeIcon(String imagePath, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
            Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            System.err.println("이미지 로드 중 오류 발생: " + imagePath);
            return null;
        }
    }
}
