package org.cobee.server.bookmark.repository;

import org.cobee.server.bookmark.domain.Bookmark;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberAndRecruitPost(Member member, RecruitPost recruitPost);

}
