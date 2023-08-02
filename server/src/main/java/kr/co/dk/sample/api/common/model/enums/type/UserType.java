package kr.co.dk.sample.api.common.model.enums.type;

import lombok.Getter;

@Getter
public enum UserType {
    ROLE_USER("USER", "일반유저"),
    ROLE_ADMIN("ADMIN", "어드민");

    private final String role;
    private final String description;

    UserType(String role, String description) {
        this.role = role;
        this.description = description;
    }
}
