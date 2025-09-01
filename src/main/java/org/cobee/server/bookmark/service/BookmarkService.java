package org.cobee.server.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.bookmark.domain.Bookmark;
import org.cobee.server.bookmark.dto.BookmarkListResponse;
import org.cobee.server.bookmark.dto.BookmarkResponse;
import org.cobee.server.bookmark.repository.BookmarkRepository;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final RecruitPostRepository recruitPostRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public BookmarkResponse addBookmark(Member member, Long postId) {
        RecruitPost recruitPost = recruitPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // bookmark 중복 체크
        checkExceptionBookmark(member, recruitPost);
        // bookmark 생성
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .recruitPost(recruitPost)
                .build();
        // bookmark 저장
        bookmarkRepository.save(bookmark);
        return BookmarkResponse.from(recruitPost, member, bookmark);
    }

    public void checkExceptionBookmark(Member member, RecruitPost recruitPost) {
        // 중복 bookmark check
        if (bookmarkRepository.existsByMemberAndRecruitPost(member, recruitPost)) {
            throw new CustomException(ErrorCode.BOOKMARK_ALREADY_EXIST);
        }
        // member가 작성한 북마크인 경우
        else if (recruitPost.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.CANNOT_BOOKMARK_OWN_POST);
        }
    }

    @Transactional(readOnly = true)
    public BookmarkListResponse getBookmarkList(Member member) {
        List<Bookmark> bookmarkList = bookmarkRepository.findByMemberOrderByCreatedAtDesc(member);
        List<BookmarkResponse> bookmarkResponses = bookmarkList.stream()
                .map(bookmark ->
                        BookmarkResponse.from(bookmark.getRecruitPost(), member, bookmark))
                .collect(Collectors.toList());
        return BookmarkListResponse.from(bookmarkResponses);
    }

    @Transactional
    public BookmarkResponse deleteBookmark(Long bookmarkId, Member member) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));
        if (!bookmark.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.BOOKMARK_ACCESS_DENIED);
        }
        BookmarkResponse bookmarkResponse = BookmarkResponse.from(bookmark.getRecruitPost(), member, bookmark);
        bookmarkRepository.delete(bookmark);
        return bookmarkResponse;
    }

    public void deleteAllBookmark(Member member) {

    }
}
