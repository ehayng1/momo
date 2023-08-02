package kr.co.dk.sample.api.app.auth.dto.kakao;

import lombok.Data;

@Data
public class KakaoProperty {
    private String has_email;
    private String email_needs_agreement;
    private String is_email_valid;
    private String is_email_verified;
    private String email;
    private String has_birthday;
    private String birthday_needs_agreement;
    private String birthday;
    private String birthday_type;
    private String has_gender;
    private String gender_needs_agreement;
    private String gender;
}
