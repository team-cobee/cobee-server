package org.cobee.server.publicProfile.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.publicProfile.domain.PublicProfile;
import org.cobee.server.publicProfile.dto.PublicProfileRequestDto;
import org.cobee.server.publicProfile.repository.PublicProfileRepository;
import org.cobee.server.member.Member;
import org.cobee.server.member.MemberRepository;
import org.cobee.server.publicProfile.dto.PublicProfileResponseDto;
import org.cobee.server.publicProfile.dto.PublicProfileUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublicProfileService {

    private final PublicProfileRepository publicProfileRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createPublicProfile(Long memberId, PublicProfileRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));

        PublicProfile publicProfile = new PublicProfile(
                requestDto.info(),
                requestDto.mLifestyle(),
                requestDto.mPersonality(),
                requestDto.mSmoking(),
                requestDto.mSnoring(),
                requestDto.mPet()
        );
        member.setPublicProfile(publicProfile);
        publicProfileRepository.save(publicProfile);
    }

    @Transactional(readOnly = true)
    public PublicProfileResponseDto getPublicProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));
        PublicProfile publicProfile = member.getPublicProfile();

        return new PublicProfileResponseDto(
                member.getId(),
                member.getName(),
                member.getGender(),
                publicProfile.getInfo(),
                publicProfile.getMLifestyle(),
                publicProfile.getMPersonality(),
                publicProfile.getMSmoking(),
                publicProfile.getMSnoring(),
                publicProfile.getMPet()
        );
    }

    @Transactional
    public void updatePublicProfile(Long memberId, PublicProfileUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));
        PublicProfile publicProfile = member.getPublicProfile();
        publicProfile.update(
                requestDto.info(),
                requestDto.mLifestyle(),
                requestDto.mPersonality(),
                requestDto.mSmoking(),
                requestDto.mSnoring(),
                requestDto.mPet()
        );
    }
}
