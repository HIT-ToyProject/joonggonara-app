package com.hit.joonggonara.service;

import com.hit.joonggonara.custom.login.CustomUserProvider;
import com.hit.joonggonara.dto.TokenDto;
import com.hit.joonggonara.dto.request.LoginRequest;
import com.hit.joonggonara.dto.response.TokenResponse;
import com.hit.joonggonara.error.CustomException;
import com.hit.joonggonara.error.errorCode.UserErrorCode;
import com.hit.joonggonara.jwt.JwtUtil;
import com.hit.joonggonara.repository.RefreshTokenRepository;
import com.hit.joonggonara.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchRuntimeException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private CustomUserProvider userProvider;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("[Service] login 성공 테스트")
    void loginSuccessTest() throws Exception
    {
        //given
        TokenDto tokenDto = createTokenDto();
        Authentication authentication = createUsernamePasswordAuthenticationToken();
        LoginRequest loginRequest = createLoginRequest();
        given(userProvider.authenticate(any())).willReturn(authentication);
        given(jwtUtil.getToken(any(), any(), any())).willReturn(tokenDto);
        given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(false);
        given(refreshTokenRepository.save(any())).willReturn(any());
        //when
        TokenResponse tokenResponse = loginService.login(loginRequest);
        //then
        assertThat(tokenResponse.accessToken()).isEqualTo(tokenDto.accessToken());
        assertThat(tokenResponse.refreshToken()).isEqualTo(tokenDto.refreshToken());
       

        then(userProvider).should().authenticate(any());
        then(jwtUtil).should().getToken(any(),any(),any());
        then(refreshTokenRepository).should().existsByRefreshToken(any());
        then(refreshTokenRepository).should().save(any());
    }
    
    @Test
    @DisplayName("[Service] 존재 하지 않은 권한 정보 테스트")
    void notExistAuthorization() throws Exception
    {
        //given
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@naver.com", "abc1234");
        LoginRequest loginRequest = createLoginRequest();
        given(userProvider.authenticate(any())).willReturn(authentication);
        //when
        CustomException exception =
                (CustomException)catchRuntimeException(()->loginService.login(loginRequest));
        //then
        assertThat(exception.getErrorCode().getHttpStatus())
                .isEqualTo(UserErrorCode.NOT_EXIST_AUTHORIZATION.getHttpStatus());
        assertThat(exception.getMessage()).isEqualTo(UserErrorCode.NOT_EXIST_AUTHORIZATION.getMessage());
        then(userProvider).should().authenticate(any());
    }
    
    @Test
    @DisplayName("[Service] 이미 존재 하는 회원 테스트")
    void alreadyLoggedInUserTest() throws Exception
    {
        //given
        TokenDto tokenDto = createTokenDto();
        Authentication authentication = createUsernamePasswordAuthenticationToken();
        LoginRequest loginRequest = createLoginRequest();
        given(userProvider.authenticate(any())).willReturn(authentication);
        given(jwtUtil.getToken(any(), any(), any())).willReturn(tokenDto);
        given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(true);
        //when
       CustomException exception =
               (CustomException)catchRuntimeException(()->loginService.login(loginRequest));
        //then
        assertThat(exception.getErrorCode().getHttpStatus()).isEqualTo(UserErrorCode.ALREADY_LOGGED_IN_USER.getHttpStatus());
        assertThat(exception.getMessage()).isEqualTo(UserErrorCode.ALREADY_LOGGED_IN_USER.getMessage());

        then(userProvider).should().authenticate(any());
        then(jwtUtil).should().getToken(any(),any(),any());
        then(refreshTokenRepository).should().existsByRefreshToken(any());
    }

    private LoginRequest createLoginRequest() {
        return LoginRequest.of("test@naver.com", "abc1234");
    }

    private TokenDto createTokenDto() {
        return TokenDto.of("accessToken", "refreshToken", "test@naver.com");
    }

    private Authentication createUsernamePasswordAuthenticationToken() {

        return new UsernamePasswordAuthenticationToken(
                "test@naver.com",
                "abc1234",
                Collections.singleton(new SimpleGrantedAuthority(Role.USER.name())));
    }
}