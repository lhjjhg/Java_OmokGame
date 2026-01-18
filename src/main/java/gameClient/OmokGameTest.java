package gameClient;

import javax.swing.*;
import java.awt.*;

public class OmokGameTest {
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("Omok Game Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // 창 크기 설정
        frame.setLayout(null);
        frame.setResizable(false);

        // BoardCanvas 생성
        ClientBack mockClientBack = new ClientBack(); // 가짜 ClientBack 객체 (테스트용)
        BoardCanvas boardCanvas = new BoardCanvas(mockClientBack, "TestRoom", "TestPlayer");

        boardCanvas.setBounds(10, 10, 531, 531);
        frame.add(boardCanvas);

        // 오른쪽에 간단한 UI 추가 (준비, 항복, 나가기 버튼)
        JButton readyBtn = new JButton("준비");
        readyBtn.setBounds(560, 20, 200, 30);
        readyBtn.addActionListener(e -> System.out.println("[테스트]: 준비 버튼 클릭됨"));
        frame.add(readyBtn);

        JButton surrenderBtn = new JButton("항복");
        surrenderBtn.setBounds(560, 60, 200, 30);
        surrenderBtn.addActionListener(e -> System.out.println("[테스트]: 항복 버튼 클릭됨"));
        frame.add(surrenderBtn);

        JButton leaveBtn = new JButton("나가기");
        leaveBtn.setBounds(560, 100, 200, 30);
        leaveBtn.addActionListener(e -> {
            System.out.println("[테스트]: 나가기 버튼 클릭됨");
            frame.dispose();
        });
        frame.add(leaveBtn);

        // 테스트 UI 표시
        frame.setVisible(true);

        // 디버그 메시지 출력
        System.out.println("[디버깅]: OmokGameTest 실행");
    }
}
