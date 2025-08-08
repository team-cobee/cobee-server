package org.cobee.server.ocr.dto;

import lombok.Data;

@Data
public class OcrResponse {
    private boolean success;
    private String message;
    private OcrData data;

    @Data
    public static class OcrData {
        private String name;           // 이름
        private String residentNumber; // 주민등록번호
        private String address;        // 주소
        private String issueDate;      // 발급일자
        private String authority;      // 발급기관
    }
}