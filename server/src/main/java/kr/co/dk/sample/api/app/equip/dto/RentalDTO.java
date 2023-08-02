package kr.co.dk.sample.api.app.equip.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RentalDTO {
    private String title;
    private String divideFirst;
    private String divideSecond;
    private String location;
    private String locationDetail;
    private String periodStart;
    private String periodEnd;
    private String name;
    private String phone;
    private String etc;
//    private MultipartFile constructionImage;
    private List<MultipartFile> constructionImage = null;
    private List<String> fileExt;
    private int modify = 0;
    private int idx = 0;
}
