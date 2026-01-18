package gameClient;

import javax.swing.JFrame;
import Function.Data;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TestBoardCanvas {
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("Omok Board Test");

        // JFrame 기본 설정
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600); // 프레임 크기 설정

        // 테스트용 데이터 초기화
        initializeGameData();

        // BoardCanvas 생성
        ClientBack clientBack = null; // 통신 객체가 없으므로 null
        BoardCanvas boardCanvas = new BoardCanvas(clientBack, "TestRoom", "TestPlayer");

        // MouseListener 추가: 클릭한 위치에 돌 배치
        boardCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 클릭 좌표를 보드 위치로 변환
                int x = (e.getX() - 4) / 35; // 보드 이미지의 시작 좌표를 보정
                int y = (e.getY() - 4) / 35;

                // 좌표가 유효한 범위인지 확인
                if (x >= 0 && x < 15 && y >= 0 && y < 15) {
                    // 빈 칸인지 확인
                    if (Data.chessBoard[x][y] == 0) {
                        // 현재 턴의 돌을 놓음
                        Data.chessBoard[x][y] = (Data.turn == 0) ? Data.BLACK : Data.WHITE;
                        Data.last = y * 15 + x; // 마지막 돌 위치 저장

                        // 돌 이미지 갱신 (black2 -> black, white2 -> white)
                        boardCanvas.paintBoardImage();
                        boardCanvas.repaint();

                        // 턴 변경
                        Data.turn = (Data.turn == 0) ? 1 : 0;

                        System.out.println("[정보]: 돌을 놓았습니다. 위치: (" + x + ", " + y + "), 현재 턴: " +
                                (Data.turn == 0 ? "흑돌" : "백돌"));
                    } else {
                        System.out.println("[오류]: 이미 돌이 놓인 위치입니다!");
                    }
                } else {
                    System.out.println("[오류]: 보드 범위를 벗어났습니다!");
                }
            }
        });

        // BoardCanvas를 JFrame에 추가
        frame.add(boardCanvas);

        // 프레임 표시
        frame.setVisible(true);
    }

    /**
     * 게임 데이터를 초기화하는 메서드
     */
    private static void initializeGameData() {
        // 15x15 오목판 초기화
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Data.chessBoard[i][j] = 0; // 모든 칸을 빈칸으로 설정
            }
        }
        Data.last = -1; // 마지막 돌 초기화
        Data.turn = 0; // 흑돌부터 시작
        Data.myChess = Data.BLACK; // 플레이어 기본 돌 색상: 흑돌
        Data.oppoChess = Data.WHITE; // 상대방 돌 색상: 백돌
    }
}
