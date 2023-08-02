package kr.co.dk.sample.api.app.auth.dto;

import lombok.Getter;

@Getter
public enum AuthPlatform {

    KAKAO("KAKAO", "카카오"),
    NAVER("NAVER", "네이버"),
    ;

    private String value;
    private String description;

    AuthPlatform(String value, String description) {
        this.value = value;
        this.description = description;
    }

}
