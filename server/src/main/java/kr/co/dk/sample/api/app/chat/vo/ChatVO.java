package kr.co.dk.sample.api.app.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatVO {

    // 유저의 이름을 저장하기 위한 변수
    private String userName;
    // 메시지의 내용을 저장하기 위한 변수
    private String content;
    private String token;

    public ChatVO(String userName, String content) {
        this.userName = userName;
        this.content = content;
    }

}
