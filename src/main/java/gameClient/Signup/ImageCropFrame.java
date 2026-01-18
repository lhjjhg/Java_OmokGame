package gameClient.Signup;

import javax.swing.*;

import java.awt.*; 
import java.awt.event.*; 
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageCropFrame extends JFrame {

	private BufferedImage image;
	
	// 자르기 영역 변수
	private Point startPoint;
	private Point endPoint;
	private Rectangle cropRect;
	private boolean cropping = false;

	private JoinFrame joinFrame;

	public ImageCropFrame(BufferedImage image, JoinFrame joinFrame) {
		this.image = image;
		this.joinFrame = joinFrame;

		// 프레임 설정
		setTitle("이미지 자르기");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(new Dimension(image.getWidth(), image.getHeight()));

		// 마우스 리스너 추가
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				startPoint = e.getPoint(); 
				cropping = true; 
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				cropping = false; 
				endPoint = e.getPoint();
				cropRect = new Rectangle(Math.min(startPoint.x, endPoint.x), Math.min(startPoint.y, endPoint.y),
						Math.abs(startPoint.x - endPoint.x), Math.abs(startPoint.y - endPoint.y));
				repaint(); 
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (cropping) {
					endPoint = e.getPoint();
					repaint(); 
				}
			}
		});

		// 자르기 버튼 추가
		JButton cropButton = new JButton("Crop Image");
		cropButton.addActionListener(e -> cropImage()); 
		add(cropButton, BorderLayout.SOUTH);

		setVisible(true);
	}

	// 선택된 영역을 잘라내는 메서드
	public void cropImage() {
		if (cropRect != null && image != null) {
			try {
				BufferedImage croppedImage = image.getSubimage(cropRect.x, cropRect.y, cropRect.width, cropRect.height);
				joinFrame.setProfileImage(croppedImage);
				joinFrame.setProfileImageBytes(convertImageToBytes(croppedImage));
				dispose(); 
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error cropping image.");
			}
		}
	}

	// Convert BufferedImage to byte array
	private byte[] convertImageToBytes(BufferedImage img) {
	    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	        ImageIO.write(img, "jpg", baos);
	        baos.flush();
	        return baos.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// 이미지를 프레임에 그리기
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		}

		// 자르기 영역을 표시
		if (startPoint != null && endPoint != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLUE); // 자를 영역을 빨간색으로 표시
			g2d.setStroke(new BasicStroke(2));
			cropRect = new Rectangle(Math.min(startPoint.x, endPoint.x), Math.min(startPoint.y, endPoint.y),
					Math.abs(startPoint.x - endPoint.x), Math.abs(startPoint.y - endPoint.y));
			g2d.draw(cropRect);
		}
	}
}