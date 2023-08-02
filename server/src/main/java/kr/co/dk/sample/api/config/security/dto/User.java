package kr.co.dk.sample.api.config.security.dto;

//import com.upts.backend.common.model.enums.status.UserStatus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class User implements Serializable {

    // 0323 old legacy
//    private long idx;
//    private String authIdx;
//    private String authPlatform;

//    private String name;
//    private String zipCode;
//    private String addressLocation;
//    private String addressDetail;
//    private int age;
//    private int point;

    private int user_idx;
    private String userEmail;
    private String userName;
    private String userPhone;
    private String userLocation;
    private String userType;
    private int userStatus;
    private String tokenAccess;
    private String tokenRefresh;
    private String tokenFcm;
    private String codeSmsCert;
    private String codeBlockchain;

    private List<Authority> authorities = new ArrayList<Authority>();

//    @Convert(converter = UserStatus.Converter.class)
//    private UserStatus status;

    public static User of(int userIdx) {
        User user = new User();
        user.setUser_idx(userIdx);
        return user;
    }

    public void addAuthority(Authority authority) {
        authority.setUser(this);
        this.authorities.add(authority);
    }
}
