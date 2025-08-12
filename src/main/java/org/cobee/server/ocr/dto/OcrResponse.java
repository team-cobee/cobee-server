package org.cobee.server.ocr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OcrResponse {
    private boolean success;
    private String filename;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("raw_text")
    private String rawText;
    @JsonProperty("structured_data")
    private StructuredData structuredData;
    private String error;

    @Data
    public static class StructuredData {
        private String name;
        @JsonProperty("ssn_front")
        private String ssnFront;
        private String address;
        @JsonProperty("ssn_back_first")
        private String ssnBackFirst;
        private String gender;
    }

    public boolean isSuccess() {
        return success && structuredData != null && structuredData.getName() != null;
    }

    public String getMessage() {
        if (error != null) {
            return error;
        }
        return isSuccess() ? "OCR 처리 성공" : "OCR 처리 실패";
    }

    public String getName() {
        return structuredData != null ? structuredData.getName() : null;
    }

    public String getSsnFront() {
        return structuredData != null ? structuredData.getSsnFront() : null;
    }

    public String getAddress() {
        return structuredData != null ? structuredData.getAddress() : null;
    }

    public String getSsnBackFirst() {
        return structuredData != null ? structuredData.getSsnBackFirst() : null;
    }

    public String getGender() {
        return structuredData != null ? structuredData.getGender() : null;
    }

    public String getFullResidentNumber() {
        if (getSsnFront() != null && getSsnBackFirst() != null) {
            return getSsnFront() + "-" + getSsnBackFirst() + "XXXXXX";
        }
        return null;
    }
}