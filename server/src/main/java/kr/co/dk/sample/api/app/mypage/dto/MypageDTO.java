package kr.co.dk.sample.api.app.mypage.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MypageDTO {

    private MultipartFile user_image;

    ///s

//      private String workIdx;
}
