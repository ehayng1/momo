package kr.co.dk.sample.api.app.chat.controller;

import kr.co.dk.sample.api.app.chat.dto.ChatMessageDTO;
import kr.co.dk.sample.api.app.chat.service.ChatService;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StompChat {

    private static final Logger log = LogManager.getLogger(StompChat.class);

    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    private Map<String, Integer> sessions = new HashMap<>();

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ChatService chatService;

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping(value = "/api/v1/chat/enter")
    public void enter(ChatMessageDTO message) throws Exception {
        int roomNum = Integer.parseInt(message.getRoomId());
//        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        if (sessions.get(Integer.toString(roomNum)) == null) {
            sessions.put(Integer.toString(roomNum), 1);
        } else {
            sessions.put(Integer.toString(roomNum), sessions.get(Integer.toString(roomNum)) + 1);
        }

        message.setPeoples(sessions.get(message.getRoomId()));
        template.convertAndSend("/sub/api/v1/chat/room/" + message.getRoomId(), message);
    }

    // 메세지 발송
    @MessageMapping(value = "/api/v1/chat/message")
    public void message(ChatMessageDTO message) throws Exception {
        System.out.println("asd");
        System.out.println(message);
        int roomNum = Integer.parseInt(message.getRoomId());
        message.setPeoples(sessions.get(Integer.toString(roomNum)));
        // 오픈채팅이 아닐 경우 DB에 메세지 저장
        if (roomNum > 6) {
            Map<String, Object> token = jwtTokenProvider.getJwtInfo(message.getToken());
            message.setSendUserIdx(token.get("mb_idx").toString());
            // recvUserIdx는 채팅방 입장할 때 설정해야함
            int result = chatService.insertMessage(message);
        }
        template.convertAndSend("/sub/api/v1/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/api/v1/chat/exit")
    public void exit(ChatMessageDTO message) throws Exception {
        int roomNum = Integer.parseInt(message.getRoomId());
        sessions.put(Integer.toString(roomNum), sessions.get(Integer.toString(roomNum)) - 1);
        message.setPeoples(sessions.get(Integer.toString(roomNum)));
        template.convertAndSend("/sub/api/v1/chat/room/" + roomNum, message);
    }
}
