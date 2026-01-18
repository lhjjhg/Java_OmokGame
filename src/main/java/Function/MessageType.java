package Function;

/**
 * 서버와 클라이언트 간 메시지 타입을 정의하는 열거형.
 * 각 메시지 타입은 특정 동작을 수행하기 위한 명령으로 사용됩니다.
 */
public enum MessageType {
    /* --- 방 관련 --- */
    CreateRoom,           // 방 생성
    ResetRoomList,        // 방 목록 초기화
    RemoveRoom,           // 방 삭제
    EnterRoom,            // 방 입장
    LeaveRoom,            // 방 나가기

    /* --- 유저 관련 --- */
    ResetUserList,        // 유저 목록 초기화
    RemoveUser,           // 유저 제거
    SearchUser,           // 유저 검색
    ShowUserInfo,         // 유저 정보 표시
    UserLeft,             // 유저 나감
    EnterUser,            // 유저 입장
    LeaveUser,            // 유저 퇴장
    EditUser,             // 유저 정보 수정

    /* --- 관리자 관련 --- */
    AdminTool,            // 관리자 도구 호출

    /* --- 채팅 관련 --- */
    CHAT,                 // 일반 채팅 메시지
    Whisper,              // 귓속말 메시지
    EMOJI,                // 이모티콘 메시지

    /* --- 검색 관련 --- */
    SEARCH_REQUEST,       // 검색 요청
    SEARCH_RESPONSE,      // 검색 응답

    /* --- 1:1 채팅 관련 --- */
    EnterPrivateRoom,     // 1:1 채팅방 입장
    PrivateChat,          // 1:1 채팅 메시지
    OneToOneChatRequest,   // 1:1 채팅 요청
    
    /* 오목 관련 TYPE */
    OmokEnterRoom, // 오목 방 입장 클라 (currentUser : RoomName), 서버 (currentUser : RoomName : PLAYER or OBSERVER)
    OmokUpdateRoomUserList, // 오목 방 유저 리스트 업데이트 object<List>
    OmokCreateRoom, // 오목 방 생성, 생성한 사람이 바로 들어가고 생성한 사람이 나가면 방 제거 (Creator:RoomName, Object)
    OmokLeaveRoom, // 오목 방에서 유저가 나감 (sender:roomName:PLAYER or OBSERVER)
    OmokSurrender, // 오목 항복했을 때 (currentUser:RoomName)
    OmokUpdatePlayer, // 오목 유저의 상태를 변경 (currentUser:PLAYER or OBSERVER)
    OmokRemoveRoom, // 방 제거될 때 (RoomName)
    OmokUpdateRoom,
    OmokChat, // 오목 채팅 (roomName:sender:message)
    /* 오목 게임 TYPE */
    OmokReady, // 오목 게임 준비 (roomName:sender)
    OmokStart, // 오목 게임 시작 (Object)
    OmokPlaceStone, // 돌 두었을 때 (roomName:sender,Object)
    
}