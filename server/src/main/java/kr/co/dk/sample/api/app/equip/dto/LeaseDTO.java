package kr.co.dk.sample.api.app.equip.dto;

import lombok.Data;

@Data
public class LeaseDTO {
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
}
