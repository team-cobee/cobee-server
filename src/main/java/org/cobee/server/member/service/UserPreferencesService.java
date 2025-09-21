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


    public UserPreferencesResponseDto createUserPreferences(Long memberId, UserPreferencesRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 이미 선호도가 등록되어 있는지 확인
        if (userPreferencesRepository.existsByMemberId(memberId)) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_ERROR);
        }

        // UserPreferences 엔티티 생성
        UserPreferences userPreferences = UserPreferences.builder()
                .gender(requestDto.getPreferredGender())
                .lifestyle(requestDto.getLifestyle())
                .personality(requestDto.getPersonality())
                .isSmoking(requestDto.getSmokingPreference())
                .isSnoring(requestDto.getSnoringPreference())
                .cohabitantCount(requestDto.getCohabitantCount())
                .hasPet(requestDto.getPetPreference())
                .info(requestDto.getAdditionalInfo())
                .member(member)
                .build();

        UserPreferences savedPreferences = userPreferencesRepository.save(userPreferences);

        log.info("사용자 선호도 등록 완료 - memberId: {}, preferencesId: {}", memberId, savedPreferences.getId());

        return UserPreferencesResponseDto.from(savedPreferences);

    }

    @Transactional(readOnly = true)
    public UserPreferencesResponseDto getUserPreferences(Long memberId) {
        UserPreferences userPreferences = userPreferencesRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_PREFERENCES_NOT_FOUND));

        return UserPreferencesResponseDto.from(userPreferences);
    }

    public UserPreferencesResponseDto updateUserPreferences(Long memberId, UserPreferencesRequestDto requestDto) {
        UserPreferences userPreferences = userPreferencesRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_PREFERENCES_NOT_FOUND));

        // 선호도 정보 업데이트
        userPreferences.updatePreferences(
                requestDto.getPreferredGender(),
                requestDto.getLifestyle(),
                requestDto.getPersonality(),
                requestDto.getSmokingPreference(),
                requestDto.getSnoringPreference(),
                requestDto.getCohabitantCount(),
                requestDto.getPetPreference(),
                requestDto.getAdditionalInfo()
        );

        UserPreferences updatedPreferences = userPreferencesRepository.save(userPreferences);

        log.info("사용자 선호도 수정 완료 - memberId: {}, preferencesId: {}", memberId, updatedPreferences.getId());

        return UserPreferencesResponseDto.from(updatedPreferences);
    }

    public void deleteUserPreferences(Long memberId) {
        UserPreferences userPreferences = userPreferencesRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_PREFERENCES_NOT_FOUND));

        userPreferencesRepository.delete(userPreferences);

        log.info("사용자 선호도 삭제 완료 - memberId: {}, preferencesId: {}", memberId, userPreferences.getId());
    }
}
