package org.cobee.server.ocr.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.enums.SocialType;

@Getter
@Builder
public class OcrVerificationResponseDto {
    private Long id;
    private String name;
    private String email;
    private String birthDate;
    private String gender;
    private SocialType socialType;
    private Boolean isCompleted;
    private Boolean ocrValidation;
    private Boolean isHost;
    
    // OCR 인증 관련 추가 정보
    private String verificationMessage;
    private String verificationStatus; // SUCCESS, FAILED

    public static OcrVerificationResponseDto from(Member member, String message, String status) {
        return OcrVerificationResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .socialType(member.getSocialType())
                .isCompleted(member.getIsCompleted())
                .ocrValidation(member.getOcrValidation())
                .isHost(member.getIsHost())
                .verificationMessage(message)
                .verificationStatus(status)
                .build();
    }
    
    public static OcrVerificationResponseDto success(Member member) {
        return from(member, "주민등록증 인증이 성공적으로 완료되었습니다.", "SUCCESS");
    }
    
    public static OcrVerificationResponseDto failed(Member member, String errorMessage) {
        return from(member, errorMessage, "FAILED");
    }
}