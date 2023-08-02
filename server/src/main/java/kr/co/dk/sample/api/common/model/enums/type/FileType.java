package kr.co.dk.sample.api.common.model.enums.type;

import lombok.Getter;

@Getter
public enum FileType {
    IMAGE(1, "image", "이미지"),
    VIDEO(2, "video", "비디오"),
    DOCUMENT(3, "document", "문서");

    private final Integer value;
    private final String uploadPath;
    private final String description;

    FileType(Integer value, String uploadPath, String description) {
        this.value = value;
        this.description = description;
        this.uploadPath = uploadPath;
    }
}
