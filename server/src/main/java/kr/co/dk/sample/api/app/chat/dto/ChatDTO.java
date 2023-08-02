package kr.co.dk.sample.api.app.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    private int chatroom_idx;
    private String sendName;
//    private int sendName;


    private int recvName;
    private String content;
}
