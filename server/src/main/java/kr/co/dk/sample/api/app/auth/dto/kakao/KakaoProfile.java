package kr.co.dk.sample.api.app.auth.dto.kakao;

import lombok.Data;

@Data
public class KakaoProfile {
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;
}
