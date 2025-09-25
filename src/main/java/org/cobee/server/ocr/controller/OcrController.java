package org.cobee.server.ocr.controller;

import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.ocr.dto.OcrTask;
import org.cobee.server.ocr.service.OcrService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ocr")
@Slf4j
@RequiredArgsConstructor
public class OcrController {

  private final OcrService ocrService;

  @Operation(summary = "주민등록증 OCR 인증 요청", description = "비동기 OCR 인증 작업을 시작하고 작업 ID(taskId)를 반환합니다.")
  @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<Map<String, String>> startOcrVerification(
          @RequestPart("image") MultipartFile imageFile,
          @AuthenticationPrincipal PrincipalDetails principalDetails) {

      if (imageFile.isEmpty() || imageFile.getContentType() == null || !imageFile.getContentType()
              .startsWith("image/")) {
          return ApiResponse.failure("유효하지 않은 이미지 파일입니다.", "400", "INVALID_FILE");
      }

      Member member = principalDetails.getMember();

      try {
          // 1. 새로운 작업을 등록하고 taskId를 받음
          String taskId = ocrService.registerNewTask();

          // 2. 비동기 작업 시작 (파일 내용을 byte[]로 변환하여 전달)
          byte[] fileBytes = imageFile.getBytes();
          String originalFilename = imageFile.getOriginalFilename();
          ocrService.processOcrVerificationAsync(taskId, member.getId(), fileBytes, originalFilename);

            // 3. taskId를 즉시 클라이언트에 반환
          return ApiResponse.success("인증 처리가 시작되었습니다.", "202", Map.of("taskId", taskId));

      } catch (IOException e) {
          log.error("파일을 읽는 중 오류가 발생했습니다.", e);
          return ApiResponse.failure("파일 처리 중 오류가 발생했습니다.", "500", "FILE_PROCESSING_ERROR");
      }
    }

  @Operation(summary = "OCR 인증 상태 조회", description = "taskId를 이용해 비동기 작업의 현재 상태와 결과를 조회합니다.")
  @GetMapping("/verify/status/{taskId}")
  public ApiResponse<OcrTask> getOcrVerificationStatus(@PathVariable String taskId) {
    OcrTask task = ocrService.getTaskStatus(taskId);

    if (task == null) {
      return ApiResponse.failure("유효하지 않은 작업 ID 입니다.", "404", "TASK_NOT_FOUND");
    }

    return ApiResponse.success("상태 조회가 완료되었습니다.", "200", task);
  }

  /**
   * OCR 인증 상태 조회 : 만약 로그인 후 신분증 인증 하기 전 미리 나갔을 경우 대비. ocr 인증 로직으로 넘어가야 하므로..
   */
  @GetMapping("/status")
  public ApiResponse<OcrStatusDto> getOcrStatus(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    Member member = principalDetails.getMember();
    boolean isVerified = member.getOcrValidation() != null && member.getOcrValidation();

    return ApiResponse.success(
        "OCR 인증 상태 조회 성공", "200",
        new OcrStatusDto(isVerified));
  }

  // 내부 DTO
  public static class OcrStatusDto {

    public final boolean ocrVerified;

    public OcrStatusDto(boolean ocrVerified) {
      this.ocrVerified = ocrVerified;
    }
  }
}