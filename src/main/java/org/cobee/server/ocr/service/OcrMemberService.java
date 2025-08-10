package org.cobee.server.ocr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.ocr.dto.OcrResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OcrMemberService {

    private final MemberRepository memberRepository;

    /**
     * OCR 결과를 Member 엔티티에 저장
     */
    public void updateMemberWithOcrData(Long memberId, OcrResponse ocrData) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // OCR 결과에서 정보 추출 및 매핑
        if (ocrData.getName() != null && ocrData.getSss_front() != null && ocrData.getSsn_back_first() != null) {
            String birthDate = extractBirthDateFromOcrData(ocrData.getSss_front(), ocrData.getSsn_back_first());
            String genderCode = convertGenderToCode(ocrData.getGender());
            
            // Member 엔티티 업데이트
            member.updateOcrValidation(ocrData.getName(), birthDate, genderCode);
            
            memberRepository.save(member);
            log.info("Member OCR 정보 업데이트 완료 - ID: {}, 이름: {}", memberId, ocrData.getName());
        }
    }

    /**
     * OCR 데이터에서 생년월일 추출 (YYYYMMDD 형식)
     */
    private String extractBirthDateFromOcrData(String sss_front, Integer ssn_back_first) {
        if (sss_front == null || sss_front.length() != 6 || ssn_back_first == null) {
            return null;
        }

        try {
            // 1,2: 1900년대, 3,4: 2000년대
            String year;
            if (ssn_back_first == 1 || ssn_back_first == 2) {
                year = "19" + sss_front.substring(0, 2);
            } else if (ssn_back_first == 3 || ssn_back_first == 4) {
                year = "20" + sss_front.substring(0, 2);
            } else {
                return null;
            }

            String month = sss_front.substring(2, 4);
            String day = sss_front.substring(4, 6);

            return year + month + day; // YYYYMMDD 형식으로 반환

        } catch (Exception e) {
            log.error("생년월일 추출 실패: sss_front={}, ssn_back_first={}", sss_front, ssn_back_first, e);
            return null;
        }
    }

    /**
     * 성별 한글을 코드로 변환
     */
    private String convertGenderToCode(String genderKorean) {
        if (genderKorean == null) {
            return null;
        }
        
        return switch (genderKorean) {
            case "남성" -> "MALE";
            case "여성" -> "FEMALE";
            default -> null;
        };
    }

    /**
     * 주민등록번호에서 생년월일 추출 (YYYYMMDD 형식) - 레거시 메서드
     */
    private String extractBirthDateFromResidentNumber(String residentNumber) {
        if (residentNumber == null || residentNumber.length() < 7) {
            return null;
        }

        try {
            String birthPart = residentNumber.substring(0, 6); // YYMMDD
            String genderDigit = residentNumber.substring(6, 7); // 7번째 자리

            // 1,2: 1900년대, 3,4: 2000년대
            String year;
            if (genderDigit.equals("1") || genderDigit.equals("2")) {
                year = "19" + birthPart.substring(0, 2);
            } else if (genderDigit.equals("3") || genderDigit.equals("4")) {
                year = "20" + birthPart.substring(0, 2);
            } else {
                return null;
            }

            String month = birthPart.substring(2, 4);
            String day = birthPart.substring(4, 6);

            return year + month + day; // YYYYMMDD 형식으로 반환

        } catch (Exception e) {
            log.error("생년월일 추출 실패: {}", residentNumber, e);
            return null;
        }
    }

    /**
     * 주민등록번호에서 성별 추출
     */
    private String extractGenderFromResidentNumber(String residentNumber) {
        if (residentNumber == null || residentNumber.length() < 7) {
            return null;
        }

        try {
            String genderDigit = residentNumber.substring(6, 7);
            
            // 1,3: 남성, 2,4: 여성
            if (genderDigit.equals("1") || genderDigit.equals("3")) {
                return "MALE";
            } else if (genderDigit.equals("2") || genderDigit.equals("4")) {
                return "FEMALE";
            }
            
            return null;
        } catch (Exception e) {
            log.error("성별 추출 실패: {}", residentNumber, e);
            return null;
        }
    }

    /**
     * 회원의 OCR 인증 상태 조회
     */
    @Transactional(readOnly = true)
    public boolean isOcrVerified(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        
        return member.getOcrValidation() != null && member.getOcrValidation();
    }
}