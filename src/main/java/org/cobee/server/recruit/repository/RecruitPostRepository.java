package org.cobee.server.recruit.repository;

import org.cobee.server.recruit.domain.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitPostRepository extends JpaRepository<RecruitPost, Long> {
}
