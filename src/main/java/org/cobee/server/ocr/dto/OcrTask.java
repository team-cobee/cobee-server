package org.cobee.server.ocr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.cobee.server.ocr.enums.OcrTaskStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 JSON에서 제외
public class OcrTask {
  private OcrTaskStatus status;
  private OcrVerificationResponseDto result; // 성공 시 결과 데이터
  private String errorMessage; // 실패 시 에러 메시지

  public OcrTask() {
    this.status = OcrTaskStatus.PENDING;
  }
}
