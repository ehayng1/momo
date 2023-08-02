package kr.co.dk.sample.api.config.rest;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;

@Getter
public class CustomClientException extends WebClientException {
    private final HttpStatus status;
    private final KakaoExceptionDetails details;

    CustomClientException(HttpStatus status, KakaoExceptionDetails details) {
        super(status.getReasonPhrase());
        this.status = status;
        this.details = details;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public KakaoExceptionDetails getDetails() {
        return details;
    }

}
