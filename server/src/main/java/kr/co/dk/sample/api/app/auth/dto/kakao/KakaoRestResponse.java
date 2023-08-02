package kr.co.dk.sample.api.app.auth.dto.kakao;

import lombok.Data;

@Data
public class KakaoRestResponse<T> {
    private String id;
    private String connected_at;
    private KakaoProperty kakaoProperty;
    private T data;
}
