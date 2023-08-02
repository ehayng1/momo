package kr.co.dk.sample.api.app.auth.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AuthEquipDTO {

    private String user_type;
    private String location;
    private String check;
    private String phone;
    private String owner_name;
    private String owner_phone;
    private String equipname;
    private String owner_category;
    private String owner_address;
    private MultipartFile owner_image;
    private String owner_career;
    private String user_idx;
    private String token;
    private String equip_spec;

    private String name;
    private String nickname;

}
