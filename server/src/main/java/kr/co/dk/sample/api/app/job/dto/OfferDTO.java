package kr.co.dk.sample.api.app.job.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OfferDTO {
    private String constructor;
    private String siteName;
    private MultipartFile jobImage;
    private String divideFirst;
    private String divideSecond;
    private String siteAddress;
    private String periodStart;
    private String detailAddress;
    private String periodEnd;
    private String phone;
    private String payment;
    private int siteIdx;
    private String etc;
    private String rest;
    private String age;
}
