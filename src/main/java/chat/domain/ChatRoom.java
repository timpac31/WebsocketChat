package chat.domain;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRoom {
	private final String id;
	private String name;
	private CopyOnWriteArrayList<String> userList = new CopyOnWriteArrayList<String>();
	
	public ChatRoom(String name) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addUser(String userId) {
		userList.add(userId);
	}
	
	public void removeUser(String userId) {
		userList.remove(userId);
	}
	
	public List<String> getUserList() {
		return userList;
	}
	
}
