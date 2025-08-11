package org.cobee.server.ocr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.ocr.dto.OcrResponse;
import org.cobee.server.ocr.dto.OcrVerificationResponseDto;
import org.cobee.server.ocr.service.OcrService;
import org.cobee.server.ocr.service.OcrMemberService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ocr")
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;
    private final OcrMemberService ocrMemberService;
    private final MemberRepository memberRepository;

    /**
     * 주민등록증 OCR 인증
     */
    @Operation(summary = "주민등록증 OCR 인증", description = "업로드된 주민등록증 이미지를 OCR로 인증하여 회원 정보를 업데이트합니다.")
    @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<OcrVerificationResponseDto> verifyIdCard(
            @Parameter(description = "주민등록증 이미지 파일", required = true)
            @RequestPart("image") MultipartFile imageFile,
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

        Member member = principalDetails.getMember();
        
        // OCR API 호출
        OcrResponse ocrResult = ocrService.processIdCard(imageFile);

        if (ocrResult.isSuccess()) {
            try {
                // Member 엔티티에 OCR 결과 저장
                ocrMemberService.updateMemberWithOcrData(member.getId(), ocrResult);
                
                // 업데이트된 멤버 정보 다시 조회
                Member updatedMember = memberRepository.findById(member.getId())
                        .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
                
                OcrVerificationResponseDto responseDto = OcrVerificationResponseDto.success(updatedMember);
                return ApiResponse.success("주민등록증 인증이 완료되었습니다.", "200", responseDto);
            } catch (Exception e) {
                return ApiResponse.failure("OCR 결과 저장 중 오류가 발생했습니다.", "500", "DATA_SAVE_ERROR");
            }
        } else {
            return ApiResponse.failure("주민등록증 인증에 실패했습니다: " + ocrResult.getMessage(), "400", "OCR_FAILED");
        }
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