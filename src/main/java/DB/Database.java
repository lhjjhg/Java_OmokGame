package DB;

import java.sql.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Database {
	private static final String URL = "jdbc:mysql://localhost:3306/Java_db";
	private static final String USER = "root";
	private static final String PASSWD = "123456";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

	public Connection con = null;
	public Statement stmt = null;
	public PreparedStatement pstmt = null;
	public ResultSet result = null;

	// Database Connect
	public Database() {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWD);
			stmt = con.createStatement();
			System.out.println("MySQL 서버 연동 성공");
		} catch (Exception e) {
			System.out.println("MySQL 서버 연동 실패 > " + e.toString());
		}
	}

	// 데이터베이스 연결 반환
	public Connection getConnection() {
		return con;
	}

	/* (MainFrame) 로그인 정보 확인 */
	public boolean logincheck(String _i, String _p) {
		boolean flag = false;

		String id = _i;
		String pw = _p;

		try {
			String checkStr = "SELECT password FROM users WHERE id='" + id + "'";
			result = stmt.executeQuery(checkStr);

			while (result.next()) {
				if (pw.equals(result.getString("password"))) {
					flag = true;
					System.out.println("로그인 성공");
				} else {
					flag = false;
					System.out.println("로그인 실패");
				}
			}
		} catch (Exception e) {
			flag = false;
			System.out.println("로그인 실패 > " + e.toString());
		}
		return flag;
	}

	/* joinFrame 회원 정보 추가 */
	public boolean joinCheck(String id, String password, String name, String nickName, 
							String phone, String email,String address, String birthdate, 
							String gender, byte[] imageBytes) {

		boolean flag = false;
		try {
			// 입력된 날짜를 MySQL 형식인 yyyy-MM-dd로 변환
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy년MM월dd일");
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = inputFormat.parse(birthdate); // 사용자로부터 받은 birthdate 문자열을 Date 객체로 변환
			String formattedBirthdate = outputFormat.format(date); // MySQL에 넣을 수 있도록 yyyy-MM-dd 형식으로 변환

			String insertStr = "INSERT INTO users (id, password, name, nickName, phone, email, address, birthdate ,gender, profile_image) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(insertStr);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			pstmt.setString(3, name);
			pstmt.setString(4, nickName);
			pstmt.setString(5, phone);
			pstmt.setString(6, email);
			pstmt.setString(7, address);
			pstmt.setString(8, formattedBirthdate);
			pstmt.setString(9, gender);
			pstmt.setBytes(10, imageBytes);

			int rowsInserted = pstmt.executeUpdate(); // 추가된 행 수를 반환
			if (rowsInserted > 0) {
				flag = true;
				System.out.println("회원가입 성공");
			}
		} catch (Exception e) {
			flag = false;
			System.out.println("회원가입 실패 > " + e.toString());
		}
		return flag;
	}

	/* JoinFrmae 아이디 중복 체크 */
	public boolean checkIdDuplicate(String id) {
		boolean isDuplicate = false;

		try {
			String query = "SELECT COUNT(*) FROM users WHERE id = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			result = pstmt.executeQuery();

			if (result.next()) {
				int count = result.getInt(1);
				if (count > 0) {
					isDuplicate = true; // 중복된 ID가 존재함
				}
			}
		} catch (SQLException e) {
			System.out.println("ID 중복 체크 실패 > " + e.toString());
		}

		return isDuplicate;
	}
	
	/* JoinFrame 닉네임 중복 체크 */
	public boolean checkNicknameDuplicate(String nickName) {
	    boolean isDuplicate = false;
	    
	    try {
	        String query = "SELECT COUNT(*) FROM users WHERE nickName = ?";
	        pstmt = con.prepareStatement(query);
	        pstmt.setString(1, nickName);
	        result = pstmt.executeQuery();
	        
	        if (result.next()) {
	            int count = result.getInt(1);
	            if (count > 0) {
	                isDuplicate = true; // 중복된 닉네임이 존재함
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("닉네임 중복 체크 실패 > " + e.toString());
	    }
	    
	    return isDuplicate;
	}
	
	public String getNickName(String userId) {
	    String nickName = null;
	    String sql = "SELECT nickName FROM users WHERE id = '" + userId + "'";

	    try {
	        ResultSet rs = stmt.executeQuery(sql);
	        if (rs.next()) {
	            nickName = rs.getString("nickName");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return nickName;
	}
	
	//(AdminLoginFrmae) 관리자 모드 로그인 
	public boolean adminLoginCheck(String adminId, String adminPassword) {
	    boolean isValid = false;
	    try {
	        String query = "SELECT admin_password FROM admin WHERE admin_id = ?";
	        pstmt = con.prepareStatement(query);
	        pstmt.setString(1, adminId);
	        result = pstmt.executeQuery();

	        if (result.next()) {
	            if (adminPassword.equals(result.getString("admin_password"))) {
	                isValid = true; // 로그인 성공
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("관리자 로그인 확인 실패 > " + e.toString());
	    }
	    return isValid;
	}
	
	/* 여러 채팅방 채팅 메시지 저장 */
	public boolean saveChatMessage(String roomName, String senderNick, String message) {
	    boolean flag = false;
	    String insertStr = "INSERT INTO chat_messages (room_name, sender_nick, message) VALUES (?, ?, ?)";
	    
	    try {
	        pstmt = con.prepareStatement(insertStr);
	        pstmt.setString(1, roomName);
	        pstmt.setString(2, senderNick);
	        pstmt.setString(3, message);
	        
	        int rowsInserted = pstmt.executeUpdate(); // 추가된 행 수를 반환
	        if (rowsInserted > 0) {
	            flag = true;
	            System.out.println("채팅 메시지 저장 성공");
	        }
	    } catch (SQLException e) {
	        flag = false;
	        System.out.println("채팅 메시지 저장 실패 > " + e.toString());
	    }
	    return flag;
	}
	
	 /* 캐릭터 저장 */
    public boolean saveCharacter(String nickName, String characterName) {
        boolean flag = false;
        try {
            String query = "UPDATE users SET character_name = ? WHERE nickName = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, characterName);
            pstmt.setString(2, nickName);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                flag = true;
                System.out.println("캐릭터 저장 성공: " + characterName);
            }
        } catch (SQLException e) {
            System.out.println("캐릭터 저장 실패 > " + e.toString());
        }
        return flag;
    }

    /* 캐릭터 조회 */
    public String getCharacter(String nickName) {
        String character = null;
        try {
            String query = "SELECT character_name FROM users WHERE nickName = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, nickName);
            result = pstmt.executeQuery();
            if (result.next()) {
                character = result.getString("character_name");
            }
        } catch (SQLException e) {
            System.out.println("캐릭터 조회 실패 > " + e.toString());
        }
        return character;
    }
    /* 메시지 검색 */
    public List<String[]> searchMessages(String roomName, String messageContent) {
        List<String[]> messages = new ArrayList<>();
        String query = "SELECT sender_nick, message, timestamp FROM chat_messages " +
                       "WHERE room_name = ? AND message LIKE ? ORDER BY timestamp";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, roomName);
            pstmt.setString(2, "%" + messageContent + "%");
            result = pstmt.executeQuery();
            while (result.next()) {
                String[] row = new String[3];
                row[0] = result.getString("sender_nick"); // 닉네임
                row[1] = result.getString("message");     // 메시지 내용
                row[2] = result.getString("timestamp");   // 타임스탬프
                messages.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
	/* (메세지 검색)방 이름 찾기 */
    public List<String> getRoomList() {
        List<String> roomNames = new ArrayList<>();
        String query = "SELECT DISTINCT room_name FROM chat_messages";
        try {
            result = stmt.executeQuery(query);
            while (result.next()) {
                roomNames.add(result.getString("room_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomNames;
    }
    
    /* 승리 횟수 업데이트 */
    public boolean updateWin(String nickName) {
        boolean flag = false;
        try {
            String query = "UPDATE users SET win = win + 1 WHERE nickName = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, nickName);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                flag = true;
                System.out.println("승리 업데이트 성공: " + nickName);
            }
        } catch (SQLException e) {
            System.out.println("승리 업데이트 실패 > " + e.toString());
        }
        return flag;
    }

    /* 패배 횟수 업데이트 */
    public boolean updateLose(String nickName) {
        boolean flag = false;
        try {
            String query = "UPDATE users SET lose = lose + 1 WHERE nickName = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, nickName);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                flag = true;
                System.out.println("패배 업데이트 성공: " + nickName);
            }
        } catch (SQLException e) {
            System.out.println("패배 업데이트 실패 > " + e.toString());
        }
        return flag;
    }
    
    /* 비밀번호 업데이트 */
    public boolean updatePassword(String id, String newPassword) {
        boolean flag = false;
        String query = "UPDATE users SET password = ? WHERE id = ?";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, newPassword); // 새 비밀번호 설정
            pstmt.setString(2, id); // 대상 사용자 ID 설정

            int rowsUpdated = pstmt.executeUpdate(); // 업데이트된 행 수 반환
            if (rowsUpdated > 0) {
                flag = true; // 업데이트 성공
                System.out.println("비밀번호 업데이트 성공: " + id);
            }
        } catch (SQLException e) {
            System.out.println("비밀번호 업데이트 실패 > " + e.toString());
        }

        return flag; // 성공 여부 반환
    }
    
    /* 프로필 가져오기 */
    public String[] getProfile(String nickName) {
        String[] profile = new String[5];
        String query = "SELECT nickName, profile_image, character_name, win, lose FROM users WHERE nickName = ?";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, nickName);
            result = pstmt.executeQuery();
            if (result.next()) {
                profile[0] = result.getString("nickName"); // 닉네임
                profile[1] = result.getString("profile_image"); // 프로필 이미지
                profile[2] = result.getString("character_name"); // 캐릭터 이미지 이름
                profile[3] = String.valueOf(result.getInt("win")); // 승리 횟수
                profile[4] = String.valueOf(result.getInt("lose")); // 패배 횟수
            }
        } catch (SQLException e) {
            System.out.println("프로필 정보 조회 실패 > " + e.toString());
        }
        return profile;
    }
    
    /* 순위 가져오기 */
    public List<String[]> getRanking() {
        List<String[]> rankingList = new ArrayList<>();
        String query = "SELECT nickName, win, lose, " +
                       "CASE WHEN (win + lose) > 0 THEN (win / (win + lose) * 100) ELSE 0 END AS winRate " +
                       "FROM users ORDER BY winRate DESC, win DESC";
        try {
            result = stmt.executeQuery(query);
            while (result.next()) {
                String nickName = result.getString("nickName");
                String win = result.getString("win");
                String lose = result.getString("lose");
                String winRate = String.format("%.2f%%", result.getDouble("winRate"));
                rankingList.add(new String[]{nickName, win, lose, winRate});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rankingList;
    }
    
    /* 아이디 찾기 */
    public String findIdByNameAndEmail(String name, String email) {
        String foundId = null;
        String query = "SELECT id FROM users WHERE name = ? AND email = ?";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            result = pstmt.executeQuery();
            if (result.next()) {
                foundId = result.getString("id");
            }
        } catch (SQLException e) {
            System.out.println("아이디 찾기 실패 > " + e.getMessage());
        }
        return foundId;
    }

    /* 비밀번호 찾기 */
    public String findPasswordByIdAndEmail(String id, String email) {
        String foundPassword = null;
        String query = "SELECT password FROM users WHERE id = ? AND email = ?";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setString(2, email);
            result = pstmt.executeQuery();
            if (result.next()) {
                foundPassword = result.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("비밀번호 찾기 실패 > " + e.getMessage());
        }
        return foundPassword;
    }

    

	
	// 자원 해제 메소드
	public void close() {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			System.out.println("자원 해제 실패 > " + e.toString());
		}
	}

}
