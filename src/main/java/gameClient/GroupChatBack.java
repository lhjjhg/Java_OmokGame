package gameClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import DB.Database;
import Function.MessageDTO;
import Function.MessageType;

public class GroupChatBack extends Thread {
    private String nickName, roomName, ipAddress;
    private int portNum;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private GroupChatLayout groupChatLayout;
    private ArrayList<String> nickNameList = new ArrayList<>();

    public void setGui(GroupChatLayout groupChatLayout) {
        this.groupChatLayout = groupChatLayout;
        this.groupChatLayout.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    if (socket != null)
                        socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void setUserInfo(String nickName, String roomName, String ipAddress, int portNum) {
        this.nickName = nickName;
        this.roomName = roomName;
        this.ipAddress = ipAddress;
        this.portNum = portNum;
    }

    public void leaveRoom() {
        try {
            sendMessage(new MessageDTO(MessageType.LeaveRoom, nickName)); // 닉네임 전송
            System.out.println("[클라이언트]: 방에서 나갔습니다.");
        } catch (Exception e) {
            System.out.println("Error leaving room: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
            if (socket != null)
                socket.close();
            System.out.println("Disconnected from the server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ipAddress, portNum);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(new MessageDTO(MessageType.EnterUser, nickName));
            out.flush();

            while (true) {
                MessageDTO receivedMessage = (MessageDTO) in.readObject();
                switch (receivedMessage.getType()) {
                    case ResetUserList:
                        groupChatLayout.userModel.removeAllElements();
                        nickNameList.add(receivedMessage.getContent());
                        groupChatLayout.resetUserListArea(nickNameList);
                        break;
                    case CHAT:
                        groupChatLayout.appendMessage(receivedMessage.getContent());
                        break;
                    case UserLeft:
                        groupChatLayout.appendMessage(receivedMessage.getContent());
                        break;
                    case LeaveRoom:
                        handleLeaveRoomMessage(receivedMessage.getContent());
                        break;
                    default:
                        System.out.println("Unhandled message type: " + receivedMessage.getType());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleLeaveRoomMessage(String content) {
        groupChatLayout.appendMessage("[방 삭제]: " + content);
    }

    public void sendMessage(MessageDTO message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
