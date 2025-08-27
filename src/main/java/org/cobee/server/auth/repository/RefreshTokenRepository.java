package org.cobee.server.auth.repository;

import org.cobee.server.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> { // 비관계형이므로 CrudRepository 사용
    Optional<RefreshToken> findById(String memberId);
    // 로그아웃 시 삭제
    void deleteById(String memberId);

    boolean existsById(String memberId);
}
