package kr.co.dk.sample.api.common.model.enums.type;

import lombok.Getter;

@Getter
public enum SortType {
    DESC("desc", "내림차순"),
    ASC("asc", "오름차순");

    private final String en;
    private final String kr;

    SortType(String en, String kr) {
        this.en = en;
        this.kr = kr;
    }
}
