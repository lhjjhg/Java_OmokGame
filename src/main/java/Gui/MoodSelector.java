package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoodSelector extends JFrame {
   
	private JComboBox<String> moodComboBox;

    private String[] moods = {"행복", "슬픔", "놀람", "분노", "무표정"};
    private String[] imagePaths = {
        "happy.jpg",  
        "sad.jpg",    
        "surprised.jpg",
        "angry.jpg",  
        "neutral.jpg" 
    };

    public MoodSelector() {
        setTitle("기분 및 표정 선택");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        moodComboBox = new JComboBox<>(moods);
        moodComboBox.setSelectedIndex(0);  // 기본 선택
        moodComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMoodImage();  // 선택된 감정에 맞는 이미지 표시
            }
        });

        add(new JLabel("감정을 선택하세요:"));
        add(moodComboBox);

        setSize(300, 100);
        setVisible(true);
    }

    //감정에 맞는 창을 띄우는 메서드 
    private void showMoodImage() {
    	 int selectedIndex = moodComboBox.getSelectedIndex();
         String imagePath = "src/main/java/Image/" + imagePaths[selectedIndex];  
        ImageIcon moodImage = new ImageIcon(imagePath);

        JFrame imageFrame = new JFrame("감정 이미지");
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageFrame.setLayout(new BorderLayout());
        
        JLabel imageLabel = new JLabel(moodImage);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        Image img = moodImage.getImage();
        Image resizedImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(resizedImg));

        imageFrame.add(imageLabel, BorderLayout.CENTER);

        imageFrame.setSize(300, 300);
        imageFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new MoodSelector();
    }
}
