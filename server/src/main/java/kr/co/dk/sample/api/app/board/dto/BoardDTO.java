package kr.co.dk.sample.api.app.board.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardDTO {
    private String issueTitle;
    private String issueContent;
//    private MultipartFile issueImage;
    private List<MultipartFile> issueImage = null;
    private String additional;
    private List<String> fileExt;
}
