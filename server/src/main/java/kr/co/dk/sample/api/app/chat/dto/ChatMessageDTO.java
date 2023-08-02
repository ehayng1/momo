package kr.co.dk.sample.api.app.chat.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private String roomId;
    private String writer;
    private String message;
    private String date;
    private int peoples;

    private String sendUserIdx;
    private String recvUserIdx;

    private String token;
}
