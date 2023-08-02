package kr.co.dk.sample.api.app.equip.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EquipRegiDTO {
    private String owner_name;
    private String owner_phone;
    private String equip_spec;

    private MultipartFile owner_image;
    private String owner_category;

}
