package chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import chat.domain.ChatMessage;
import chat.domain.ChatRoom;
import chat.repository.ChatRoomRepository;

@Controller
public class ChatController {
	private final SimpMessagingTemplate template;
	private final ChatRoomRepository roomRepository;

    @Autowired
    public ChatController(SimpMessagingTemplate template, ChatRoomRepository roomRepository) {
        this.template = template;
        this.roomRepository = roomRepository;
    }
    
	
    @MessageMapping("/chat/createRoom")
	public void createRoom(ChatMessage msg) {
    	ChatRoom room = new ChatRoom(msg.getRoomId());
    	room.addUser(msg.getUserId());
    	roomRepository.addChatRoom(room);
    	
    	msg.setAction(ActionType.ENTER_ROOM.code);
    	msg.setMsg("님이 입장하셨습니다.");
    	send(msg);
	}
    
    @MessageMapping("/chat/message")
    public void send(ChatMessage msg) {
    	template.convertAndSend("/topic/chat/room/" + msg.getRoomId(), msg);
    }
    
    @MessageMapping("/chat/join")
    private void join(ChatMessage msg) {
    	roomRepository.getChatRoom(msg.getRoomId()).addUser(msg.getUserId());
    	msg.setAction(ActionType.ENTER_ROOM.code);
    	msg.setMsg("님이 입장하셨습니다.");
    	send(msg);
    }
    
    @MessageMapping("/chat/exitRoom")
    private void exitRoom(ChatMessage msg) {
    	roomRepository.getChatRoom(msg.getRoomId()).removeUser(msg.getUserId());
    	msg.setAction(ActionType.EXIT_ROOM.code);
    	msg.setMsg("님이 퇴장하셨습니다.");
    	send(msg);
    }
    
    
    @ResponseBody
    @GetMapping("/rooms")
    public List<ChatRoom> getRoomList() {
    	return roomRepository.getChatRoomList();
    }
	
    @ResponseBody
    @GetMapping("/users")
    public List<String> getUserList(String roomName) {
    	return roomRepository.getUserListInRoom(roomName);
    }
    
}
