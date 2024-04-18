package com.hit.joonggonara.entity;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// TTL 만료시간 = 2주
@RedisHash(value = "Refresh_token", timeToLive = 1000 * 60 * 60 * 24 * 14)
public class RefreshToken {

    @Id
    private Long id;
    private String email;
    @Indexed
    private String refreshToken;

    @Builder
    public RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
