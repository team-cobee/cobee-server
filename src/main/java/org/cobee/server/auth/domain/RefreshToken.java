package org.cobee.server.auth.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("refreshToken") // redis 에 저장되는 엔티티를 의미
public class RefreshToken {

    @Id
    private String id;
    private String token; // refreshToken

    @TimeToLive // 자동 만료 시간 설정
    private Long expiration;

    public static RefreshToken of(Long memberId, String token, Long expiration) {
        return RefreshToken.builder()
                .id(String.valueOf(memberId))
                .token(token)
                .expiration(expiration)
                .build();
    }
}
