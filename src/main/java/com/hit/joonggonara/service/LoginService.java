package com.hit.joonggonara.service;

import com.hit.joonggonara.custom.login.CustomUserProvider;
import com.hit.joonggonara.dto.TokenDto;
import com.hit.joonggonara.dto.request.LoginRequest;
import com.hit.joonggonara.dto.response.TokenResponse;
import com.hit.joonggonara.entity.RefreshToken;
import com.hit.joonggonara.error.CustomException;
import com.hit.joonggonara.error.errorCode.UserErrorCode;
import com.hit.joonggonara.util.JwtUtil;
import com.hit.joonggonara.repository.RefreshTokenRepository;
import com.hit.joonggonara.type.LoginType;
import com.hit.joonggonara.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LoginService {

    private final CustomUserProvider userProvider;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest loginRequest){

        // 초기 인증 정보를 넣는다.
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        Authentication authenticate = userProvider.authenticate(authentication);

        // 회원 Role을 꺼내옴
        String r = authenticate.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst()
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_EXIST_AUTHORIZATION));
        Role role = Role.checkRole(r);

        // access token, refresh token 생성
        TokenDto token = createToken((String) authenticate.getPrincipal(), role);

        return TokenResponse.ToResponse(token);
    }

    private TokenDto createToken(String email, Role role) {

        TokenDto token = jwtUtil.getToken(email, role, LoginType.GENERAL);
        saveRefreshToken(email, token.refreshToken());
        return token;
    }

    private void saveRefreshToken(String email, String refreshToken) {
        boolean isTrue = refreshTokenRepository.existsByRefreshToken(refreshToken);

        // refreshToken이 존재 할경우 이미 로그인한 유저로 판단
        if(isTrue){
            throw new CustomException(UserErrorCode.ALREADY_LOGGED_IN_USER);
        }

        RefreshToken refreshTokenEntity = RefreshToken.builder().email(email).refreshToken(refreshToken).build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

}
