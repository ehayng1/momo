package kr.co.dk.sample.api.config.rest;

import lombok.Data;

@Data
public class KakaoExceptionDetails {
    private String msg;
    private int code;
}
