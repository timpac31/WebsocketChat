package chat.controller;

public enum ActionType {
	ENTER_ROOM("enter_room", "채팅방 입장"),
	EXIT_ROOM("exit_room", "채팅방 퇴장"),
	INVALID("invalid", "잘못된 요청");
	
	public final String code;
	public final String desc;
	
	ActionType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static ActionType from(String code){
		if(code==null || code.equals("")){
			return INVALID;
		}
		for(ActionType t : ActionType.values()){
			if(code.equals(t.code)){
				return t;
			}
		}
		throw new IllegalArgumentException(code + " type이 없습니다.");
	}

}
