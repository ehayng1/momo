package kr.co.dk.sample.api.app.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class RatingDTO {
    @Schema(description = "사이트 인덱스", defaultValue = "1")
    private int site_idx;
    //    @Schema(description = "사용자 인덱스", defaultValue = "1")
//    private int user_idx;
    @Schema(description = "업무 유형", defaultValue = "기술공/전체")
    private String user_divide_first;
    @Schema(description = "업무경력", defaultValue = "1")
    private String user_divide_second;
    @Schema(description = "현장평가 - 안전")
    private int site_rating_safe;
    @Schema(description = "현장평가 - 복지")
    private int site_rating_welfare;
    @Schema(description = "현장평가 - 강도")
    private int site_rating_hard;
    @Schema(description = "현장평가 - 급여")
    private int site_rating_payment;
    @Schema(description = "현장평가 - 식사")
    private int site_rating_meal;
    @Schema(description = "현장평가 - 휴식")
    private int site_rating_rest;
    @Schema(description = "추천하기", allowableValues = {"0", "1"})
    private int site_rating_up;
    @Schema(description = "아쉬워요", allowableValues = {"0", "1"})
    private int site_rating_down;
    @Schema(description = "종합 한줄평")
    private String site_rating_comment;
}
