package org.cobee.server.ocr.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.ocr.dto.OcrResponse;
import org.cobee.server.ocr.service.OcrService;
import org.cobee.server.ocr.service.OcrMemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;
    private final OcrMemberService ocrMemberService;

    /**
     * 주민등록증 OCR 인증
     */
    @PostMapping("/verify")
    public ApiResponse<OcrResponse.OcrData> verifyIdCard(
            @RequestParam("image") MultipartFile imageFile,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 파일 유효성 검사
        if (imageFile.isEmpty()) {
            return ApiResponse.failure("이미지 파일을 업로드해주세요.", "400", "INVALID_FILE");
        }

        // 파일 형식 검사 (이미지만 허용)
        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ApiResponse.failure("이미지 파일만 업로드 가능합니다.", "400", "INVALID_FILE_TYPE");
        }

        try {
            // OCR API 호출
            OcrResponse ocrResult = ocrService.processIdCard(imageFile);

            if (ocrResult.isSuccess()) {
                // Member 엔티티에 OCR 결과 저장
                Member member = principalDetails.getMember();
                ocrMemberService.updateMemberWithOcrData(member.getId(), ocrResult.getData());

                return ApiResponse.success("주민등록증 인증이 완료되었습니다.", "200", ocrResult.getData());
            } else {
                return ApiResponse.failure("주민등록증 인증에 실패했습니다: " + ocrResult.getMessage(), "400", "OCR_FAILED");
            }

        } catch (Exception e) {
            return ApiResponse.failure("OCR 처리 중 오류가 발생했습니다.", "500", "INTERNAL_ERROR");
        }
    }

    /**
     * OCR 인증 상태 조회
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