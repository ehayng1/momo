package kr.co.dk.sample.api.app.board.dto;

import lombok.Data;

@Data
public class ReplyDTO {
    private int issueIdx;
    private String replyContent;
}
