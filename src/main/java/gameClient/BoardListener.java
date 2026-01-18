package gameClient;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Function.Data;
import Function.MessageDTO;
import Function.MessageType;

public class BoardListener extends MouseAdapter {

    private final BoardCanvas boardCanvas;
    private final ClientBack clientBack;
    private final String roomName;
    private final String playerName;

    public BoardListener(BoardCanvas boardCanvas, ClientBack clientBack, String roomName, String playerName) {
        this.boardCanvas = boardCanvas;
        this.clientBack = clientBack;
        this.roomName = roomName;
        this.playerName = playerName;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // 마우스 클릭 위치
        int mouseX = e.getX();
        int mouseY = e.getY();

        // 오목판 좌표로 변환
        int xIndex = (mouseX - 20) / 35;
        int yIndex = (mouseY - 20) / 35;

        // 범위 확인
        if (xIndex < 0 || xIndex >= 15 || yIndex < 0 || yIndex >= 15) {
            System.out.println("[오류]: 오목판 범위를 벗어난 클릭입니다.");
            return;
        }

        // 이미 돌이 놓인 위치인지 확인
        if (Data.chessBoard[xIndex][yIndex] != 0) {
            System.out.println("[오류]: 이미 돌이 놓인 위치입니다.");
            return;
        }

        // 현재 턴인지 확인
        if (Data.turn == 0 && Data.myChess != Data.BLACK) {
            System.out.println("[오류]: 흑돌의 턴입니다. 자신의 차례를 기다리세요.");
            return;
        } else if (Data.turn == 1 && Data.myChess != Data.WHITE) {
            System.out.println("[오류]: 백돌의 턴입니다. 자신의 차례를 기다리세요.");
            return;
        }

        // 서버에 돌 놓기 메시지 전송
        int[] stonePosition = { xIndex, yIndex };
        clientBack.sendMessage(new MessageDTO(MessageType.OmokPlaceStone, playerName + ":" + roomName, stonePosition));

        // 오목판 업데이트
        Data.chessBoard[xIndex][yIndex] = Data.myChess;
        Data.last = 15 * yIndex + xIndex;
        boardCanvas.paintBoardImage();
        boardCanvas.repaint();

        // 턴 변경
        Data.turn = (Data.turn == 0) ? 1 : 0;
    }
}