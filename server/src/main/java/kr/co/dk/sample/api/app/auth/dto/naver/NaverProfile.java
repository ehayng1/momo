package kr.co.dk.sample.api.app.auth.dto.naver;

import lombok.Data;

@Data
public class NaverProfile {
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;
}
