package kr.co.dk.sample.api.config.security.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class Authority implements Serializable {
    private int idx;
    private String role = "USER";
    private User user;

    public Authority(int idx, String role, User user) {
        this.idx = idx;
        this.role = role;
        this.user = user;
    }
}
