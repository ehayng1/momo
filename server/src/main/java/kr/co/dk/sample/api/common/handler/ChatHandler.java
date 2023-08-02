package kr.co.dk.sample.api.common.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler extends TextWebSocketHandler {
    private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionList.add(session);

        // 스프링 시큐리티 사용시 가능한 메소드
        // System.out.println(session.getPrincipal().getName() + "의 입장");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage msg) throws Exception {
        for (WebSocketSession s : sessionList) {
//            s.sendMessage(msg.getPayload()));

            // 스프링 시큐리티 사용시 가능한 메소드
             s.sendMessage(new TextMessage(session.getPrincipal().getName() + ": " + msg.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);

        // 스프링 시큐리티 사용시 가능한 메소드
        // System.out.println(session.getPrincipal().getName() + "의 퇴장");
    }
}
