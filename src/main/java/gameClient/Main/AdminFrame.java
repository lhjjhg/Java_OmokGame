package gameClient.Main;

import DB.Database;
import gameClient.Signup.JoinFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminFrame extends JFrame {

	private Database db = new Database();
	private JPanel adminPanel;
	private JTable userTable;
	private DefaultTableModel tableModel;
	private boolean isDataLoaded = false; // 데이터 로드 여부를 확인하는 변수
	
	public AdminFrame() {
		setTitle("관리자 화면");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);

		adminPanel = new JPanel();
		adminPanel.setLayout(new BorderLayout());
		setContentPane(adminPanel);

		// 테이블 모델 생성
		tableModel = new DefaultTableModel(new String[] { "ID", "이름", "닉네임", "이메일", "전화번호", "주소", "생년월일", "성별" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // 셀 편집 불가
			}
		};
		userTable = new JTable(tableModel);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(userTable);
		adminPanel.add(scrollPane, BorderLayout.CENTER);

		// 버튼 패널
		JPanel buttonPanel = new JPanel();
		adminPanel.add(buttonPanel, BorderLayout.SOUTH);

		JButton addUserBtn = new JButton("회원 추가"); 
		buttonPanel.add(addUserBtn);
		
		JButton updateBtn = new JButton("회원 정보 수정");
		buttonPanel.add(updateBtn);

		JButton deleteBtn = new JButton("회원 삭제");
		buttonPanel.add(deleteBtn);

		JButton restoreBtn = new JButton("회원 복원");
		buttonPanel.add(restoreBtn);

		JButton refreshBtn = new JButton("새로 고침");
		buttonPanel.add(refreshBtn);

		JButton exitBtn = new JButton("나가기");
		buttonPanel.add(exitBtn);

		// AdminFrame 수정 부분
		// 검색 패널 추가
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JComboBox<String> searchComboBox = new JComboBox<>(new String[]{"ID", "닉네임", "이메일", "전화번호"});
		JTextField searchField = new JTextField(15);
		JButton searchButton = new JButton("검색");
		JButton resetButton = new JButton("초기화");
		searchPanel.add(new JLabel("검색 기준:"));
		searchPanel.add(searchComboBox);
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.add(resetButton);
		adminPanel.add(searchPanel, BorderLayout.NORTH);
		
		// 검색 버튼 이벤트
		searchButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String selectedCriteria = searchComboBox.getSelectedItem().toString();
		        String searchValue = searchField.getText().trim();

		        if (!searchValue.isEmpty()) {
		            searchByCriteria(selectedCriteria, searchValue);
		        } else {
		            JOptionPane.showMessageDialog(null, "검색어를 입력하세요.", "검색 오류", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		// 회원 추가 버튼 이벤트
		addUserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JoinFrame().setVisible(true); // 회원가입 창 열기
			}
		});

		// 회원 정보 수정 버튼 이벤트
		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = userTable.getSelectedRow();
				if (selectedRow >= 0) {
					String userId = (String) tableModel.getValueAt(selectedRow, 0);
					new ModifyInfoFrame(userId).setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "수정할 회원을 선택하세요.", "수정 실패", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// 회원 삭제 버튼 이벤트
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = userTable.getSelectedRow();
				if (selectedRow >= 0) {
					String userId = (String) tableModel.getValueAt(selectedRow, 0);
					confirmDeleteUser(userId);
				} else {
					JOptionPane.showMessageDialog(null, "삭제할 회원을 선택하세요.", "삭제 실패", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// 나가기 버튼 이벤트
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // 창 닫기
				new MainFrame(8080).setVisible(true); // MainFrame 실행 (포트 번호는 적절히 조정)
			}
		});

		// 테이블 클릭 이벤트 (행 선택)
		userTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = userTable.getSelectedRow();
				if (e.getClickCount() == 2 && selectedRow >= 0) {
					String userId = (String) tableModel.getValueAt(selectedRow, 0);
					new ModifyInfoFrame(userId).setVisible(true);
				}
			}
		});

		// 새로 고침 버튼 이벤트
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadUserData(); // 데이터 새로 고침
			}
		});

		// 회원 복원 버튼 이벤트
		restoreBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RestoreFrame().setVisible(true);
			}
		});
		
		loadUserData(); // 프로그램 시작 시 데이터 자동 로드

	}

	// 회원 정보 로드
	private void loadUserData() {
		tableModel.setRowCount(0); // 기존 데이터 삭제
		try {
			String query = "SELECT id, name, nickName, email, phone, address, birthdate, gender FROM users";
			PreparedStatement pstmt = db.getConnection().prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				String nickName = rs.getString("nickName");
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				String address = rs.getString("address");
				String birthdate = rs.getString("birthdate");
				String gender = rs.getString("gender");
				tableModel.addRow(new Object[] { id, name, nickName, email, phone, address, birthdate, gender });
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "회원 정보 로드 실패", "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 회원 삭제 확인
	private void confirmDeleteUser(String userId) {
		int confirm = JOptionPane.showConfirmDialog(this, "정말로 이 회원을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			deleteUser(userId);
		}
	}

	// 회원 삭제
	private void deleteUser(String userId) {
		try {
			// 삭제 전 deleted_users 테이블로 이동
			String moveQuery = "INSERT INTO deleted_users SELECT * FROM users WHERE id = ?";
			PreparedStatement movePstmt = db.getConnection().prepareStatement(moveQuery);
			movePstmt.setString(1, userId);
			int rowsMoved = movePstmt.executeUpdate(); 

			if (rowsMoved > 0) {
				// users 테이블에서 회원 삭제
				String deleteQuery = "DELETE FROM users WHERE id = ?";
				PreparedStatement deletePstmt = db.getConnection().prepareStatement(deleteQuery);
				deletePstmt.setString(1, userId);
				int rowsDeleted = deletePstmt.executeUpdate(); // 삭제된 행 수 확인

				if (rowsDeleted > 0) {
					JOptionPane.showMessageDialog(null, "회원 삭제 성공");
					loadUserData(); // 삭제 후 데이터 새로 고침
				} else {
					JOptionPane.showMessageDialog(null, "회원 삭제 실패", "오류", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "회원 삭제 실패: 이동할 회원이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "회원 삭제 실패: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// 검색 로직
	private void searchByCriteria(String criteria, String value) {
	    tableModel.setRowCount(0); // 기존 데이터 삭제

	    try {
	        // 검색 기준에 따른 SQL 컬럼 매핑
	        String column = switch (criteria) {
	            case "ID" -> "id";
	            case "닉네임" -> "nickName";
	            case "이메일" -> "email";
	            case "전화번호" -> "phone";
	            default -> throw new IllegalArgumentException("올바르지 않은 검색 기준입니다.");
	        };

	        // SQL 쿼리 생성
	        String query = "SELECT id, name, nickName, email, phone, address, birthdate, gender FROM users WHERE " + column + " LIKE ?";
	        PreparedStatement pstmt = db.getConnection().prepareStatement(query);
	        pstmt.setString(1, "%" + value + "%"); // 부분 일치 검색
	        ResultSet rs = pstmt.executeQuery();

	        // 검색 결과를 테이블에 추가
	        while (rs.next()) {
	            String id = rs.getString("id");
	            String name = rs.getString("name");
	            String nickName = rs.getString("nickName");
	            String email = rs.getString("email");
	            String phone = rs.getString("phone");
	            String address = rs.getString("address");
	            String birthdate = rs.getString("birthdate");
	            String gender = rs.getString("gender");
	            tableModel.addRow(new Object[]{id, name, nickName, email, phone, address, birthdate, gender});
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "회원 검색 실패: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
	    }
	}
} 
