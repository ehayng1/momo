package kr.co.dk.sample.api.common.model.enums.type;

import kr.co.dk.sample.api.common.model.enums.CodeEnum;
import lombok.Getter;

@Getter
public enum LinkType implements CodeEnum<Integer> {
    GENERAL(1, "일반상품"),
    SUBSCRIPTION(2, "구독상품"),
    TICKET(3, "티켓상품"),
    NOTICE(4, "공지사항"),
    SHOP(5, "브랜드"),
    ALARM(6, "알람");

    private final Integer value;
    private final String description;

    LinkType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
