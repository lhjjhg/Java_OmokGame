package gameClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Function.MessageDTO;
import Function.MessageType;

public class GroupChatLayout extends JFrame {

    String nickName, roomName, ipAddress;
    int portNum;
    GroupChatBack groupChatBack = new GroupChatBack();

    private static final long serialVersionUID = 1L;

    /* Panel */
    private JPanel MainPanel;
    private JPanel ChatPanel;
    private JPanel UserPanel;

    public JTextArea ChatArea;
    public JTextArea RoomNameArea;

    DefaultListModel<String> userModel;
    public JList<String> UserList;

    private JLabel UserL;
    private JTextField ChatField;

    private JButton SendBtn;
    private JButton exitBtn;
    private JButton GameBtn;
    private LineBorder bb = new LineBorder(Color.black, 2, false);

    public GroupChatBack getGroupChatBack() {
        return groupChatBack;
    }

    public GroupChatLayout(String nickName, String roomName, String ipAddress, int portNum) {
        this.nickName = nickName;
        this.roomName = roomName;
        this.portNum = portNum;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        MainPanel = new JPanel();
        MainPanel.setBackground(new Color(255, 250, 240));
        MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(MainPanel);
        MainPanel.setLayout(null);

        setTitle(roomName + "방");
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        ChatPanel = new JPanel();
        ChatPanel.setBackground(Color.WHITE);
        ChatPanel.setBounds(10, 44, 440, 512);
        MainPanel.add(ChatPanel);
        ChatPanel.setLayout(null);

        UserPanel = new JPanel();
        UserPanel.setBackground(Color.WHITE);
        UserPanel.setBounds(475, 44, 225, 512);
        MainPanel.add(UserPanel);
        UserPanel.setLayout(null);

        ChatArea = new JTextArea();
        ChatArea.setBounds(10, 10, 420, 492);
        ChatArea.setBorder(bb);
        ChatArea.setEditable(false);
        ChatPanel.add(ChatArea);

        RoomNameArea = new JTextArea();
        RoomNameArea.setFont(new Font("한컴 고딕", Font.BOLD, 18));
        RoomNameArea.setBackground(new Color(255, 250, 240));
        RoomNameArea.setBounds(10, 10, 305, 33);
        RoomNameArea.setText(roomName);
        RoomNameArea.setEditable(false);
        MainPanel.add(RoomNameArea);

        userModel = new DefaultListModel<>();
        UserList = new JList<>(userModel);
        UserList.setBounds(10, 10, 205, 492);
        UserList.setBorder(bb);
        UserPanel.add(UserList);

        UserL = new JLabel("접속 유저");
        UserL.setBounds(475, 19, 86, 15);
        MainPanel.add(UserL);

        ChatField = new JTextField();
        ChatField.setBounds(10, 566, 305, 39);
        MainPanel.add(ChatField);
        ChatField.setColumns(10);

        SendBtn = new JButton("보내기");
        SendBtn.setBounds(336, 566, 114, 39);
        MainPanel.add(SendBtn);

        GameBtn = new JButton("게임 시작");
        GameBtn.setBounds(567, 566, 133, 39);
        MainPanel.add(GameBtn);

        exitBtn = new JButton("나가기");
        exitBtn.setBounds(475, 566, 86, 39);
        MainPanel.add(exitBtn);

        // 메시지 전송 이벤트
        ChatField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });

        SendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groupChatBack.leaveRoom(); // 방 나가기 처리
                GroupChatLayout.this.dispose(); // 현재 방 화면 닫기
            }
        });




        // 서버와 연결
        groupChatBack.setGui(this);
        groupChatBack.setUserInfo(nickName, roomName, ipAddress, portNum);
        groupChatBack.start();
    }

    /* 채팅 메시지 전송 메서드 */
    private void sendChatMessage() {
        String message = ChatField.getText().trim();
        if (message.length() > 0) {
            groupChatBack.sendMessage(
                    new MessageDTO(MessageType.CHAT, "[" + nickName + "]: " + message));
            ChatField.setText(null);
        }
    }

    /* 유저 목록 갱신 */
    public void resetUserListArea(ArrayList<String> nickNameList) {
        userModel.removeAllElements();
        for (String nickName : nickNameList) {
            userModel.addElement(nickName);
        }
    }

    /* 채팅 메시지 추가 */
    public void appendMessage(String message) {
        ChatArea.append(message + "\n");
        ChatArea.setCaretPosition(ChatArea.getDocument().getLength()); // 스크롤 자동 하단 이동
    }
}
