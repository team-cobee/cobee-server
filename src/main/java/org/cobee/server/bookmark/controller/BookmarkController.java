package org.cobee.server.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.bookmark.dto.BookmarkListResponse;
import org.cobee.server.bookmark.dto.BookmarkResponse;
import org.cobee.server.bookmark.service.BookmarkService;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{postId}")
    public ApiResponse<BookmarkResponse> addBookmark(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "postId") Long postId
            ) {
        Member member = principalDetails.getMember();
        BookmarkResponse bookmarkResponse = bookmarkService.addBookmark(member, postId);
        return ApiResponse.success("bookmark 추가 완료", "ADD_BOOKMARK", bookmarkResponse);
    }

    @GetMapping("/")
    public ApiResponse<BookmarkListResponse> getBookmarkList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        Member member = principalDetails.getMember();
        BookmarkListResponse bookmarkList = bookmarkService.getBookmarkList(member);
        return ApiResponse.success("bookmark 목록 반환 완료", "GET_BOOKMARK", bookmarkList);
    }

    @DeleteMapping("/{bookmarkId}")
    public ApiResponse<BookmarkResponse> deleteBookmark(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "bookmarkId") Long bookmarkId
    )
    {
        Member member = principalDetails.getMember();
        BookmarkResponse bookmarkResponse = bookmarkService.deleteBookmark(bookmarkId, member);
        return ApiResponse.success("bookmark 삭제",  "DELETE_BOOKMARK", bookmarkResponse);
    }

    @DeleteMapping("/all")
    public ApiResponse<?> deleteAllBookmark(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        Member member = principalDetails.getMember();
        bookmarkService.deleteAllBookmark(member);
    }
}
