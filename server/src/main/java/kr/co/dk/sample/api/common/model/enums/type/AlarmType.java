package kr.co.dk.sample.api.common.model.enums.type;

import lombok.Getter;


@Getter
public enum AlarmType {
    POINT_ISSUE(1, "포인트 발급", true),
    EVENT_MESSAGE(2, "이벤트 메시지", true),
    DELIVERY_START(3, "상품 배송시작", false);

    private final Integer value;
    private final String description;
    private final boolean shouldPush;

    AlarmType(Integer value, String description, boolean shouldPush) {
        this.value = value;
        this.description = description;
        this.shouldPush = shouldPush;
    }
}
