package com.hit.joonggonara.repository;

import com.hit.joonggonara.config.RedisConfig;
import com.hit.joonggonara.entity.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(RedisConfig.class)
@DataRedisTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("[Redis] RefreshToken 존재 테스트")
    void existRefreshTokenTest() throws Exception
    {

        RefreshToken refreshToken = createRefreshToken();
        refreshTokenRepository.save(refreshToken);

        boolean isTrue = refreshTokenRepository.existsByRefreshToken("refreshToken");

        assertThat(isTrue).isTrue();
    }

    @Test
    @DisplayName("[Redis] 존재하지 않은 RefreshToken 테스트")
    void notExistRefreshTokenTest() throws Exception
    {

        boolean isFalse = refreshTokenRepository.existsByRefreshToken("refreshToken");

        assertThat(isFalse).isFalse();
    }

    private RefreshToken createRefreshToken() {
        return RefreshToken
                .builder()
                .email("test@naver.com")
                .refreshToken("refreshToken")
                .build();
    }

}