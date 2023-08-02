package kr.co.dk.sample.api.app.auth.dto.naver;

import lombok.Data;

@Data
public class NaverRestResponse<T> {
    private String resultcode;
    private String message;
    private NaverProperty response;
    private T data;
}
