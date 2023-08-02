package kr.co.dk.sample.api.config.security.dto;

import kr.co.dk.sample.api.common.model.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenValidation {
    private boolean success;
    private ErrorCode code;
    private boolean accessToken = false;
    private boolean refreshToken = false;
}
