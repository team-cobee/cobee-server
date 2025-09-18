package org.cobee.server.member.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.alarm.fcm.FcmTokenRequest;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.image.service.DataBucketUtil;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import static org.cobee.server.image.service.ImageValidationUtil.validateSingleImageFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DataBucketUtil dataBucketUtil;
    @Transactional
    public String updateFcmToken(Long memberId, FcmTokenRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        return member.updateFcmToken(request.getFcmToken());
    }
    @Transactional
    public String updateProfileImage(MultipartFile file, Long memberId) throws IOException {
        validateSingleImageFile(file);
        try {
            String imageUrl = dataBucketUtil.uploadImage(file);
            Member member = memberRepository.findById(memberId).orElseThrow();
            member.updateProfileUrl(imageUrl);
            memberRepository.save(member);
            return imageUrl;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }
}
