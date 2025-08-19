package org.cobee.server.recruit.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.publicProfile.dto.PublicProfileResponseDto;
import org.cobee.server.recruit.dto.ApplyAcceptRequest;
import org.cobee.server.recruit.dto.ApplyRequest;
import org.cobee.server.recruit.dto.ApplyResponse;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.service.ApplyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apply")
public class ApplyController {
    private final ApplyService applyService;
    @PostMapping()
    public ApiResponse<ApplyResponse> applyForRecruit(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                      @RequestBody ApplyRequest request){
        Long memberId = principalDetails.getMember().getId();
        ApplyResponse result = applyService.apply(memberId, request);
        return ApiResponse.success("지원이 완료되었습니다", "APPLY-001", result);

    }

    // 매칭 승인이 되면 Matching으로 바꾸기 : 이때 채팅방 초대 알림 및 매칭 알림 보내기
    @PostMapping("/accept/{applyId}")
    public ApiResponse<ApplyResponse> acceptApply(@PathVariable(name="applyId") Long applyId,
                                                  @RequestBody ApplyAcceptRequest request,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails){
        try {
            Long memberId = principalDetails.getMember().getId();
            ApplyResponse result = applyService.accept(memberId, applyId, request);
            if (request.getIsAccept()){
                return ApiResponse.success("매칭이 되었습니다.", "APPLY-002", result);
            } else {
                return ApiResponse.success("매칭이 거절되었습니다", "APPLY-003", result);
            }
        } catch (CustomException e){
            return ApiResponse.failure("", "", e.getMessage());
        }

    }

    // MatchStatus가 ON_WAIT인 것들(지원 중)
    @GetMapping("/my/onWait")
    public ApiResponse<List<RecruitResponse>> myAppliedPost(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 현재 로그인 사용자
        Long memberId = principalDetails.getMember().getId();
        List<RecruitResponse> myApplies = applyService.getMyAppliesOnWait(memberId);
        return ApiResponse.success("나의 지원 구인글 목록 조회 완료", "APPLY-004", myApplies);
    }

    // isMatched MATCHING으로 된 구인글 리스트 조회 <= 작성자가 승인 버튼 눌러서 채팅에 초대 알림 보낸 구인글 탭
    @GetMapping("/my/matching")
    public ApiResponse<List<RecruitResponse>> myOnMatchingPost(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 현재 로그인 사용자
        Long memberId = principalDetails.getMember().getId();
        List<RecruitResponse> myApplies = applyService.getMyAppliesOnMatching(memberId);
        return ApiResponse.success("나의 지원 구인글 목록 조회 완료", "APPLY-005", myApplies);
    }

    // isMatched가 MATCHED인 구인글 보기
    @GetMapping("/my/matched")
    public ApiResponse<List<RecruitResponse>> myMatchedPost(@AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memberId = principalDetails.getMember().getId();
        List<RecruitResponse> myApplies = applyService.getMyAppliesOnMatched(memberId);
        return ApiResponse.success("나의 매칭된 구인글 목록 조회 완료", "APPLY-006", myApplies);
    }


    // 나의 특정 구인글에 지원한 지원자 공개프로필 리스트
    // TODO : 멤버 처리 어떻게 할지, 고민
    @GetMapping("/{postId}")
    public ApiResponse<List<PublicProfileResponseDto>> myAppliers(@PathVariable(name="postId") Long postId,
                                                                  @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        try{
            Long memberId = principalDetails.getMember().getId();
            List<PublicProfileResponseDto> result = applyService.getMyAllPostAppliers(postId, memberId);
            if (result.isEmpty()) {
                return ApiResponse.success("지원한 멤버가 없습니다.", "APPLY-007", result);
            }
            return ApiResponse.success("지원자 조회 성공", "APPLY-008", result);
        } catch (CustomException e){
            return ApiResponse.failure("정보 요청을 다시 해주세요","APPLY-009",e.getMessage());
        }
    }



}
