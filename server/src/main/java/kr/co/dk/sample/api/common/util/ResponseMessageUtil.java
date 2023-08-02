package kr.co.dk.sample.api.common.util;

import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.rest.CustomClientException;
import org.springframework.http.ResponseEntity;


public class ResponseMessageUtil {
    private static final int INTERNAL_ERROR = 500;

    private ResponseMessageUtil() {
    }

    public static ResponseEntity<?> errorMessage(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getCode()).body(errorCode);
    }

    public static ResponseEntity<?> errorMessage(CustomClientException cce) {
        return ResponseEntity.status(cce.getStatus()).body(cce.getDetails());
    }

    public static ResponseEntity<?> errorMessage(Exception e) {
        return ResponseEntity.status(INTERNAL_ERROR).body(e.getMessage());
    }
}
