package chat.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import chat.domain.ChatRoom;

@Repository
public class ChatRoomRepository {
	private List<ChatRoom> chatRoomList = new ArrayList<ChatRoom>();
	
	public List<ChatRoom> getChatRoomList() {
		return chatRoomList;
	}
	
	public void addChatRoom(ChatRoom chatRoom) {
		chatRoomList.add(chatRoom);
	}
	
	public void removeChatRoom(ChatRoom chatRoom) {
		chatRoomList.remove(chatRoom);
	}
	
	public boolean existRoom(String name) {
		return chatRoomList.contains(name);
	}
	
	public ChatRoom getChatRoom(String name) {
		return chatRoomList.stream().filter(c -> name.equals(c.getName())).findFirst().orElse(new ChatRoom(""));
	}
	
	public List<String> getUserListInRoom(String name) {
		return getChatRoom(name).getUserList();
	}
	
}
