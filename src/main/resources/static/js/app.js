var stompClient = null;
var userId = null;
var roomId = null;
 
function setConnected(connected) {
    $('#connect').prop('disabled', connected);
    $('#disconnect').prop('disabled', !connected);
}

function connect() {
	userId = prompt("아이디를 입력하세요");
	if(userId == null || userId == "") {
		alert("아이디를 입력하세요");
		return;
	}
	//TODO: 아이디 중복체크
	
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    
    exitRoom();
    userId = null;
    roomId = null;
    console.log('Disconnected');
}

async function subscribe() {
	stompClient.subscribe('/topic/chat/room/' + roomId, function(chatMessage) {
		var msgBody = JSON.parse(chatMessage.body);
		if(msgBody.action == "enter_room" || msgBody.action == "exit_room") {
			getRoomList();
			getUserList();
		}
        showMessage(msgBody);
    });
}

function createRoom() {
	if(stompClient == null) {
		alert("커넥션을 먼저 해주세요.");
		 return;
	}
	if(roomId != null) {
		alert("이미 방에 들어와 있습니다.");
		return;
	}
	roomId = prompt("방 이름을 입력하세요");
	if(roomId == null) return;
	if(roomId == "") {
		alert("방 이름을 입력하세요");
		return;
	}
	
	//TODO: subscribe -> send 동기화
	subscribe().then(function() {
		stompClient.send('/app/chat/createRoom', {}, JSON.stringify({action: 'createRoom', roomId: roomId, userId: userId}));
	});
}

function enterRoom() {
	if(stompClient == null) {
		alert("커넥션을 먼저 해주세요.");
		 return;
	}
	if(roomId != null) {
		alert("이미 방에 들어와 있습니다.");
		return;
	}
	if($('#chatRoomList option:selected').length === 0) {
		alert('채팅방을 선택하세요');
		return;
	}
	roomId = $('#chatRoomList option:selected').val();
	
	subscribe();
	stompClient.send('/app/chat/join', {}, JSON.stringify({action: 'join', roomId: roomId, userId: userId}) );
}

function exitRoom() {
	stompClient.send('/app/chat/exitRoom', {}, JSON.stringify({action: 'exitRoom', roomId: roomId, userId: userId}));
	roomId = null;
}

function send() {
	stompClient.send('/app/chat/message', {}, JSON.stringify({roomId: roomId, msg: $('#inputMessage').val(), userId: userId}));
	$('#inputMessage').val('');
}

function showMessage(message) {
    $('#messageBox').val( $('#messageBox').val() + "\n[" + message.userId + '] ' + message.msg );
}

function getRoomList() {
	$.ajax({
		url: '/rooms',
		type: 'get'				
	}).done(function(data){
		var options = [];
		$.each(data, function(i, chatRoom) {
			options.push('<option value="'+chatRoom.name+'">' +chatRoom.name+ '</option>');
		});
		$('#chatRoomList').html(options.join(''));
	}).fail(function(error){
		console.log("getRoomList error : ", error);
	});
}

function getUserList() {
	if(roomId == null) {
		$('#userList').html('');
		return;
	}
	
	$.ajax({
		url: '/users',
		type: 'get',
		data: {'roomName' : roomId }
	}).done(function(data) {
		//console.log(data);
		var options = [];
		$.each(data, function(i, userid) {
			options.push('<option>' +userid+ '</option>');
		});
		$('#userList').html(options.join(''));
	}).fail(function(error) {
		console.log("getUserList error : ", error);
	});
}



$(function () {
    $('form').on('submit', function(e) {
        e.preventDefault();
    });
    $('#connect').click(function() { connect(); });
    $('#disconnect').click(function() { disconnect(); });
    $('#createRoom').click(function() { createRoom(); });
    $('#sendBtn').click(function() { send(); });
    $('#refreshRoom').click(function() { getRoomList(); });
    $('#enterRoom').click(function() { enterRoom(); });
    $('#exitBtn').click(function() { exitRoom(); });
    $('#inputMessage').keydown(function(e){
    	if(e.keyCode == 13) send();
    });
});
