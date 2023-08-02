package kr.co.dk.sample.api.app.labor.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LaborDTO {


    private String labor_name;
    private String labor_phone;
    private String labor_simple_explain;
    private String labor_address;
    private MultipartFile labor_image;
    private String labor_description;
    private int labor_career;

}
