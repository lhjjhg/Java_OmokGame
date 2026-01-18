//package Test;
//
//import javax.swing.*;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.BufferedImage;
//import java.util.ArrayList;
//
//public class GroupChatLayoutTest extends JFrame{
//    String nickName, ipAddress;
//    int portNum;
//    private JPanel GroupChatPanel;
//    private JTextField ChatField;
//    private JButton SendButton;
//    private JTextPane ChatTextPane;
//    JTextArea UserList;
//    private JTextArea ChatTextArea;
//    private JLabel Label;
//    private JButton WeatherButton;
//    GroupChatBack groupChatBack = new GroupChatBack();
//    BufferedImage imgBuff;
//    Color tmpColor = Color.BLACK;
//
//    public GroupChatBack getGroupChatBack() {
//        return groupChatBack;
//    }
//
//    public GroupChatLayoutTest(String nickName, String roomName, String ipAddress, int portNum) {
//        this.nickName = nickName;
//        this.portNum = portNum;
//
//        GroupChatPanel = new JPanel();
//        GroupChatPanel.setLayout(new BorderLayout()); // 레이아웃 관리자 사용
//        setContentPane(GroupChatPanel);
//
//        // Chat Area
//        ChatTextArea = new JTextArea();
//        ChatTextArea.setEditable(false);
//        ChatTextArea.setLineWrap(true);
//        JScrollPane chatScrollPane = new JScrollPane(ChatTextArea);
//        GroupChatPanel.add(chatScrollPane, BorderLayout.CENTER);
//
//        // User List Area
//        UserList = new JTextArea();
//        UserList.setEditable(false);
//        JScrollPane userScrollPane = new JScrollPane(UserList);
//        GroupChatPanel.add(userScrollPane, BorderLayout.EAST);
//
//        // Input Field and Send Button
//        JPanel inputPanel = new JPanel(new BorderLayout());
//        ChatField = new JTextField();
//        inputPanel.add(ChatField, BorderLayout.CENTER);
//        SendButton = new JButton("Send");
//        inputPanel.add(SendButton, BorderLayout.EAST);
//        GroupChatPanel.add(inputPanel, BorderLayout.SOUTH);
//
//        // Frame 설정
//        setSize(1280, 720);
//        setTitle(roomName);
//        setVisible(true);
//        setResizable(false);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Button Listener
//        SendButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String Message = ChatField.getText().trim();
//                if (Message.length() > 0) {
//                    groupChatBack.sendMessage("[" + nickName + "]: " + Message + "\n");
//                    ChatField.setText(null);
//                }
//            }
//        });
//
//        ChatField.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String Message = ChatField.getText().trim();
//                if (Message.length() > 0) {
//                    groupChatBack.sendMessage("[" + nickName + "]: " + Message + "\n");
//                    ChatField.setText(null);
//                }
//            }
//        });
//
//        // 서버와 연결
//        groupChatBack.setGui(this);
//        groupChatBack.setUserInfo(nickName, roomName, ipAddress, portNum);
//        groupChatBack.start();
//    }
//
//
//    public void appendMessage(String Message) {
//        ChatTextArea.append(Message);
//    }
//
//    public void resetUserListArea(ArrayList<String> nickNameList) {
//        // 유저목록을 유저리스트에 띄워줍니다.
//        for (String nickName : nickNameList) {
//            UserList.append(nickName + "\n");
//        }
//    }
//   
//
//}
