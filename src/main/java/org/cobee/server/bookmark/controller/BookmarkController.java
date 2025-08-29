package org.cobee.server.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.bookmark.service.BookmarkService;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{postId}")
    public ApiResponse<RecruitResponse> addBookmark(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "postId") Long postId
            ) {
        Member member = principalDetails.getMember();
        RecruitResponse recruitResponse = bookmarkService.addBookmark(member, postId);
        return ApiResponse.success("bookmark 추가 완료", "ADD_BOOKMARK", recruitResponse);
    }
}
