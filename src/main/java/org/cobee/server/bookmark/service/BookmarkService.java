package org.cobee.server.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.bookmark.domain.Bookmark;
import org.cobee.server.bookmark.repository.BookmarkRepository;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final RecruitPostRepository recruitPostRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public RecruitResponse addBookmark(Member member, Long postId) {
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
        return RecruitResponse.from(recruitPost, member);
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

}
