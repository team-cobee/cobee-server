package org.cobee.server.ocr.dto;

import lombok.Data;

@Data
public class OcrResponse {
    private String name;           // 이름
    private String sss_front;      // 주민등록번호 앞자리 (YYMMDD)
    private String address;        // 주소
    private Integer ssn_back_first; // 주민등록번호 뒷자리 첫번째 (성별 구분)
    private String gender;         // 성별 ("남성" 또는 "여성")

    // 성공 여부를 판단하는 메서드 (이름이 있으면 성공으로 간주)
    public boolean isSuccess() {
        return name != null && !name.isEmpty();
    }

    // 에러 메시지 (실패 시 사용)
    public String getMessage() {
        return isSuccess() ? "OCR 처리 성공" : "OCR 처리 실패";
    }

    // 기존 코드와의 호환성을 위한 getData 메서드
    public OcrResponse getData() {
        return this;
    }

    // 주민등록번호 전체를 조합하는 메서드
    public String getFullResidentNumber() {
        if (sss_front != null && ssn_back_first != null) {
            return sss_front + "-" + ssn_back_first + "XXXXXX"; // 뒷자리는 보안상 마스킹
        }
        return null;
    }
}