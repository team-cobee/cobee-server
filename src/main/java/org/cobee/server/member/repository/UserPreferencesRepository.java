package org.cobee.server.member.repository;

import org.cobee.server.member.domain.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
    
    Optional<UserPreferences> findByMemberId(Long memberId);
    
    boolean existsByMemberId(Long memberId);
    
    void deleteByMemberId(Long memberId);
}