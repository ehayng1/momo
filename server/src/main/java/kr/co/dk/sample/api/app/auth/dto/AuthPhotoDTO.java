package kr.co.dk.sample.api.app.auth.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AuthPhotoDTO {
    private MultipartFile user_image;
    private String nickname;
    private String user_name;
    private String randNum;
//    private MultipartFile user_basic_image;

}
