package kr.co.dk.sample.api.common.model.exception;

import kr.co.dk.sample.api.common.model.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends Exception {
    private final ErrorCode code;

    public CustomException(ErrorCode code) {
        super();
        this.code = code;
    }

    public CustomException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.code = code;
    }

    public CustomException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }

    public CustomException(Throwable cause, ErrorCode code) {
        super(cause);
        this.code = code;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
