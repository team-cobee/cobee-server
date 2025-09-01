package org.cobee.server.bookmark.repository;

import org.cobee.server.bookmark.domain.Bookmark;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberAndRecruitPost(Member member, RecruitPost recruitPost);

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.recruitPost WHERE b.member = :member ORDER BY b.createdAt DESC")
    List<Bookmark> findByMemberWithRecruitPostOrderByCreatedAtDesc(@Param("member") Member member);

    Optional<Bookmark> findById(Long id);

    int deleteByMember(Member member);
}
