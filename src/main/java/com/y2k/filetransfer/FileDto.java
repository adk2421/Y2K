package com.y2k.filetransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class FileDto {

    // 파일 uuid 값
    private String uuid;

    // 파일명
    private String fileName;

    // 파일 확장자 타입
    private String contentType;
}
