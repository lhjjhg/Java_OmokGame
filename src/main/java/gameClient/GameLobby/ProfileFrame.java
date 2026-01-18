package gameClient.GameLobby;

import javax.swing.*;
import java.awt.*;
import DB.Database;

public class ProfileFrame extends JFrame {

    public ProfileFrame(String nickName) {
        setTitle("프로필 정보");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 표시

        // 메인 패널 설정
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15)); // 패널 간 여백 추가
        mainPanel.setBackground(new Color(250, 240, 230)); // ChatLayout과 조화로운 배경색
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 패널 여백 추가
        add(mainPanel);

        // 상단: 제목
        JLabel titleLabel = new JLabel("프로필 정보", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 26));
        titleLabel.setForeground(new Color(80, 60, 50)); // 부드러운 갈색
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 중앙: 캐릭터 이미지 및 프로필 정보
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(250, 240, 230));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 캐릭터 이미지
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(255, 250, 230));
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2)); // 황금색 테두리
        JLabel characterImageLabel = new JLabel();
        characterImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        characterImageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 이미지 여백
        imagePanel.add(characterImageLabel);
        centerPanel.add(imagePanel, BorderLayout.NORTH);

        // 프로필 정보 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1, 10, 10)); // 컴포넌트 간 여백
        infoPanel.setBackground(new Color(255, 250, 230));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // 내부 여백
        centerPanel.add(infoPanel, BorderLayout.CENTER);

        JLabel nicknameLabel = new JLabel("닉네임: ");
        nicknameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        nicknameLabel.setForeground(new Color(80, 60, 50));
        infoPanel.add(nicknameLabel);

        JLabel winLabel = new JLabel("승리: ");
        winLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        winLabel.setForeground(new Color(80, 60, 50));
        infoPanel.add(winLabel);

        JLabel loseLabel = new JLabel("패배: ");
        loseLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        loseLabel.setForeground(new Color(80, 60, 50));
        infoPanel.add(loseLabel);

        JLabel winRateLabel = new JLabel("승률: ");
        winRateLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        winRateLabel.setForeground(new Color(80, 60, 50));
        infoPanel.add(winRateLabel);

        // 하단: 닫기 버튼
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(250, 240, 230));
        JButton closeButton = new JButton("닫기");
        closeButton.setPreferredSize(new Dimension(150, 40)); // 버튼 크기 조정
        styleButton(closeButton);
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 데이터베이스에서 사용자 정보 가져오기
        try {
            Database db = new Database();
            String[] profile = db.getProfile(nickName);

            if (profile != null) {
                // 캐릭터 이미지 설정
                if (profile[2] != null && !profile[2].isEmpty()) {
                    try {
                        String imagePath = profile[2];
                        ImageIcon characterIcon = new ImageIcon(getClass().getResource(imagePath));
                        Image scaledImage = characterIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // 이미지 크기 조정
                        characterImageLabel.setIcon(new ImageIcon(scaledImage));
                    } catch (Exception e) {
                        System.err.println("이미지 로드 실패: " + e.getMessage());
                        characterImageLabel.setText("기본 이미지");
                        characterImageLabel.setIcon(new ImageIcon(getClass().getResource("/Image/default.png")));
                    }
                } else {
                    characterImageLabel.setText("캐릭터 이미지 없음");
                }

                // 닉네임, 승/패, 승률 설정
                nicknameLabel.setText("닉네임: " + (profile[0] != null ? profile[0] : "알 수 없음"));
                winLabel.setText("승리: " + (profile[3] != null ? profile[3] : "0"));
                loseLabel.setText("패배: " + (profile[4] != null ? profile[4] : "0"));

                int wins = profile[3] != null ? Integer.parseInt(profile[3]) : 0;
                int losses = profile[4] != null ? Integer.parseInt(profile[4]) : 0;
                String winRate = (wins + losses > 0)
                        ? String.format("%.2f%%", (double) wins / (wins + losses) * 100)
                        : "0.00%";
                winRateLabel.setText("승률: " + winRate);

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "프로필 정보를 불러올 수 없습니다.",
                        "오류",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            db.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "프로필 정보를 가져오는 중 오류가 발생했습니다.\n" + e.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // 버튼 스타일 설정 메서드
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(200, 180, 160)); // 부드러운 갈색 계열
        button.setForeground(Color.WHITE); // 버튼 텍스트 색상
        button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(new Color(150, 130, 110))); // 테두리
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 마우스 오버 효과
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(210, 190, 170)); // 밝은 갈색
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(200, 180, 160)); // 원래 색상 복원
            }
        });
    }
}
