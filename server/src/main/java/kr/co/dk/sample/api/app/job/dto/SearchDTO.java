package kr.co.dk.sample.api.app.job.dto;

import lombok.Data;

@Data
public class SearchDTO {
    private String name;
    private String divideFirst;
    private String divideSecond;
    private String periodStart;
    private String periodEnd;
    private String phone;
    private String etc;
    private int siteIdx;
}
