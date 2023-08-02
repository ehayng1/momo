package kr.co.dk.sample.api.common.model.enums.type;

import lombok.Getter;

@Getter
public enum BoardType {

    USER_NOTICE(1, "공지"),
    USER_QNA(2, "QNA"),
    SELLER_NOTICE(3, "공지"),
    SELLER_QNA(4, "QNA"),
    TERMS(5, "약관");

    private final Integer value;
    private final String description;

    BoardType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
