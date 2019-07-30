package chat.domain;

public class ChatMessage {
	private String action; 
	private String msg;
	private String roomId;
	private String userId;
	
	public ChatMessage() {}
	
	public ChatMessage(String action, String msg, String roomId, String userId) {
		this.action = action;
		this.msg = msg;
		this.roomId = roomId;
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
