package kr.co.dk.sample.api.common.model.enums;

import lombok.Getter;

@Getter
public enum FileCategory {
    BANNER_FILE("banner"),
    COMMENT_FILE("comment"),
    BOARD_FILE("board"),
    PRODUCT_FILE("product"),
    SHOP_FILE("shop");

    private final String name;

    FileCategory(String name) {
        this.name = name;
    }

}
