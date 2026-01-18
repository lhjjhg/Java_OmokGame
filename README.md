# ⚪⚫ Omok Game (Java)

<p align="center">
  <img src="images/오목 로그인 화면.png" width="80%">
</p>

> **Java Swing과 소켓(Socket) 통신을 활용한 실시간 멀티플레이어 오목 게임입니다.** 

---

## 📌 1. 프로젝트 개요
- **개발 기간**: 2024.09 ~ 2024.12 
- **주요 목적**: 자바 네트워크 프로그래밍(Socket)과 DB(MySQL) 연동 및 GUI(Swing) 구현 능력 향상 
- **핵심 특징**: 실시간 1:1 대전, 관전 모드, 전적 관리 및 관리자 시스템 

---

## 🛠 2. Tech Stack (기술 스택)
| Category | Stack |
| :--- | :--- |
| **Language** | Java 8 |
| **GUI Library** | Java Swing, AWT |
| **Network** | Java Socket Programming (TCP/IP) |
| **Database** | MySQL (JDBC Connector) |
| **Tool** | Eclipse, Git |

---

## ✨ 3. Key Functions (주요 기능)

### 👤 회원 관리 시스템

<p align="center">
  <img src="images/오목 회원가입 화면.png" width="45%">
</p>

- **로그인 및 보안**: DB 연동을 통한 인증 및 비밀번호 찾기/재설정 기능 
- **상세 회원가입**: 아이디/닉네임 중복 확인, 비밀번호 안정성 검사, 우편번호 API 연동 
- **캐릭터 시스템**: 8종의 캐릭터 중 선택 가능하며 로비 및 게임방에 표시 

  <br> <img src="images/오목 캐릭터 변경.png" width="80%">

### 🎮 게임 플레이 및 커뮤니티

<p align="center">
  <img src="images/오목 게임 로비 화면.png" width="80%">
  <img src="images/오목 게임방 화면.png" width="80%">
</p>

- **실시간 대전**: 소켓 통신을 이용한 돌 착수 동기화 및 최근 수 빨간색 표시 
- **관전 및 승격**: 풀방(2명)일 경우 관전 모드로 진입하며, 플레이어 이탈 시 자동 승격
- **멀티 채팅**: 로비 내 전체 채팅 및 특정 유저와의 1:1 채팅 지원 

   <br> <img src="images/오목 1대1 채팅.png" width="50%">

### 🛠 관리자 모드 및 편의 기능
- **관리자 창**: 유저 정보 통합 조회 및 수정, 삭제, 복원(Soft Delete) 기능 
- **부가 기능**: 메시지 검색, 이모티콘 전송, BGM 및 다크 모드, 실시간 날씨 조회 

---

## 🧠 4. 핵심 구현 로직
- **소켓 서버**: `ServerSocket`과 다중 `Thread`를 활용하여 여러 클라이언트의 동시 접속 처리 
- **DB 연동**: JDBC를 활용하여 승률 기반 랭킹 시스템 및 채팅 로그 실시간 저장 구현 
- **이벤트 기반 프로그래밍**: Swing의 액션 리스너를 활용한 유기적인 GUI 상호작용 설계 

---
