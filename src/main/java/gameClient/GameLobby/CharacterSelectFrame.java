package gameClient.GameLobby;

import DB.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CharacterSelectFrame extends JFrame {
    private String[] characterPaths = {
            "/Image/티니핑1.png", "/Image/티니핑2.png",
            "/Image/티니핑3.png", "/Image/티니핑4.png",
            "/Image/티니핑5.png", "/Image/티니핑6.png",
            "/Image/티니핑7.png", "/Image/티니핑8.png"
    };
    private String selectedCharacterPath; // 선택된 캐릭터 경로
    private JButton selectButton; // "캐릭터 선택" 버튼
    private JLabel previewLabel; // 선택된 캐릭터 미리보기

    public CharacterSelectFrame(String nickName, String ipAddress, int portNum) {
        this(nickName, ipAddress, portNum, null); // ChatLayout 없이 호출
    }

    public CharacterSelectFrame(String nickName, String ipAddress, int portNum, ChatLayout chatLayout) {
        setTitle("캐릭터 선택");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel characterGridPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 캐릭터 버튼을 배치할 패널
        characterGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        characterGridPanel.setBackground(new Color(240, 240, 240));

        for (String characterPath : characterPaths) {
            ImageIcon icon = new ImageIcon(getClass().getResource(characterPath));
            JButton characterButton = new JButton(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            characterButton.setFocusPainted(false);
            characterButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            characterButton.setBackground(Color.WHITE);

            characterButton.addActionListener(e -> {
                selectedCharacterPath = characterPath; // 선택된 캐릭터 경로 저장
                updatePreview(); // 선택된 캐릭터 미리보기 업데이트
            });
            characterGridPanel.add(characterButton);
        }

        add(characterGridPanel, BorderLayout.CENTER);

        // 하단 패널: 캐릭터 미리보기와 버튼
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));

        // 미리보기 영역
        previewLabel = new JLabel("캐릭터를 선택하세요", SwingConstants.CENTER);
        previewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        previewLabel.setBorder(BorderFactory.createTitledBorder("미리보기"));
        previewLabel.setPreferredSize(new Dimension(200, 200));
        bottomPanel.add(previewLabel, BorderLayout.CENTER);

        // "캐릭터 선택" 버튼
        selectButton = new JButton("캐릭터 선택");
        selectButton.setFocusPainted(false);
        selectButton.setBackground(new Color(0, 120, 215));
        selectButton.setForeground(Color.WHITE);
        selectButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        selectButton.setEnabled(false); // 캐릭터를 선택하기 전까지 비활성화
        selectButton.addActionListener(e -> {
            if (selectedCharacterPath != null) {
                saveCharacterToDatabase(nickName, selectedCharacterPath); // 캐릭터 저장
                JOptionPane.showMessageDialog(null, "캐릭터가 설정되었습니다!");

                if (chatLayout != null) {
                    // ChatLayout에서 호출된 경우
                    chatLayout.setCharacterImage(selectedCharacterPath); // 캐릭터 이미지 업데이트
                    chatLayout.setVisible(true); // ChatLayout 표시
                } else {
                    // MainFrame에서 호출된 경우 ChatLayout 실행
                    ChatLayout newChatLayout = new ChatLayout(nickName, ipAddress, portNum);
                    newChatLayout.setCharacterImage(selectedCharacterPath); // 캐릭터 이미지 설정
                    newChatLayout.setVisible(true);
                }
                dispose(); // 캐릭터 선택 창 닫기
            }
        });
        bottomPanel.add(selectButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updatePreview() {
        if (selectedCharacterPath != null) {
            ImageIcon icon = new ImageIcon(getClass().getResource(selectedCharacterPath));
            Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            previewLabel.setIcon(new ImageIcon(scaledImage)); // 선택된 캐릭터 미리보기
            previewLabel.setText(null); // 텍스트 제거
            selectButton.setEnabled(true); // "캐릭터 선택" 버튼 활성화
        }
    }

    private void saveCharacterToDatabase(String nickName, String characterPath) {
        try {
//        	 String characterName = extractCharacterName(characterPath);
        	
            Database db = new Database();
            db.saveCharacter(nickName, characterPath); // 캐릭터 저장
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }	
    
//    // 경로에서 캐릭터 이름 추출 메서드
//    private String extractCharacterName(String characterPath) {
//        String[] parts = characterPath.split("/"); // '/'를 기준으로 나눔
//        String fileName = parts[parts.length - 1]; // 마지막 요소가 파일명
//        return fileName.replace(".png", "").replace(".jpg", ""); // 확장자 제거
//    }
}
