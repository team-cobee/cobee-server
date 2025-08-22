package org.cobee.server.recruit.repository;

import org.cobee.server.recruit.domain.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitPostRepository extends JpaRepository<RecruitPost, Long> {
    List<RecruitPost> findAllByMemberId(Long memberId);
}
