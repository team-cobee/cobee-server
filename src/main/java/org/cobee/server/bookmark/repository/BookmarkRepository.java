package org.cobee.server.bookmark.repository;

import org.cobee.server.bookmark.domain.Bookmark;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberAndRecruitPost(Member member, RecruitPost recruitPost);
    List<Bookmark> findByMemberOrderByCreatedAtDesc(Member member); // 최신순 정렬

    Optional<Bookmark> findById(Long id);

    int deleteByMember(Member member);
}
