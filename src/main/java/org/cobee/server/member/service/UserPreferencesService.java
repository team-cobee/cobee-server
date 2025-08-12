package org.cobee.server.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.UserPreferences;
import org.cobee.server.member.dto.UserPreferencesRequestDto;
import org.cobee.server.member.dto.UserPreferencesResponseDto;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.member.repository.UserPreferencesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;
    private final MemberRepository memberRepository;

    /**
     * 사용자 선호도 등록
     */
    public UserPreferencesResponseDto createUserPreferences(Long memberId, UserPreferencesRequestDto requestDto) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 이미 선호도가 등록되어 있는지 확인
        if (userPreferencesRepository.existsByMemberId(memberId)) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_ERROR);
        }

        // 나이 범위 검증
        validateAgeRange(requestDto.getMinAge(), requestDto.getMaxAge());
        
        // 동거 인원 범위 검증
        validateCohabitantCountRange(requestDto.getMinCohabitantCount(), requestDto.getMaxCohabitantCount());

        // UserPreferences 엔티티 생성
        UserPreferences userPreferences = UserPreferences.builder()
                .preferredGender(requestDto.getPreferredGender())
                .minAge(requestDto.getMinAge())
                .maxAge(requestDto.getMaxAge())
                .lifestyle(requestDto.getLifestyle())
                .personality(requestDto.getPersonality())
                .smokingPreference(requestDto.getSmokingPreference())
                .snoringPreference(requestDto.getSnoringPreference())
                .minCohabitantCount(requestDto.getMinCohabitantCount())
                .maxCohabitantCount(requestDto.getMaxCohabitantCount())
                .petPreference(requestDto.getPetPreference())
                .additionalInfo(requestDto.getAdditionalInfo())
                .member(member)
                .build();

        UserPreferences savedPreferences = userPreferencesRepository.save(userPreferences);
        
        log.info("사용자 선호도 등록 완료 - memberId: {}, preferencesId: {}", memberId, savedPreferences.getId());
        
        return UserPreferencesResponseDto.from(savedPreferences);
    }

    /**
     * 사용자 선호도 조회
     */
    @Transactional(readOnly = true)
    public UserPreferencesResponseDto getUserPreferences(Long memberId) {
        UserPreferences userPreferences = userPreferencesRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_PREFERENCES_NOT_FOUND));

        return UserPreferencesResponseDto.from(userPreferences);
    }

    /**
     * 사용자 선호도 수정
     */
    public UserPreferencesResponseDto updateUserPreferences(Long memberId, UserPreferencesRequestDto requestDto) {
        UserPreferences userPreferences = userPreferencesRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_PREFERENCES_NOT_FOUND));

        // 나이 범위 검증
        validateAgeRange(requestDto.getMinAge(), requestDto.getMaxAge());
        
        // 동거 인원 범위 검증
        validateCohabitantCountRange(requestDto.getMinCohabitantCount(), requestDto.getMaxCohabitantCount());

        // 선호도 정보 업데이트
        userPreferences.updatePreferences(
                requestDto.getPreferredGender(),
                requestDto.getMinAge(),
                requestDto.getMaxAge(),
                requestDto.getLifestyle(),
                requestDto.getPersonality(),
                requestDto.getSmokingPreference(),
                requestDto.getSnoringPreference(),
                requestDto.getMinCohabitantCount(),
                requestDto.getMaxCohabitantCount(),
                requestDto.getPetPreference(),
                requestDto.getAdditionalInfo()
        );

        UserPreferences updatedPreferences = userPreferencesRepository.save(userPreferences);
        
        log.info("사용자 선호도 수정 완료 - memberId: {}, preferencesId: {}", memberId, updatedPreferences.getId());
        
        return UserPreferencesResponseDto.from(updatedPreferences);
    }

    /**
     * 나이 범위 검증
     */
    private void validateAgeRange(Integer minAge, Integer maxAge) {
        if (minAge > maxAge) {
            throw new CustomException(ErrorCode.INVALID_AGE_RANGE);
        }
    }

    /**
     * 동거 인원 범위 검증
     */
    private void validateCohabitantCountRange(Integer minCount, Integer maxCount) {
        if (minCount > maxCount) {
            throw new CustomException(ErrorCode.INVALID_COHABITANT_COUNT_RANGE);
        }
    }
}