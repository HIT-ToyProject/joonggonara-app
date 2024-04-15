package com.hit.joonggonara.custom.login;

import com.hit.joonggonara.entity.Member;
import com.hit.joonggonara.error.CustomException;
import com.hit.joonggonara.error.errorCode.UserErrorCode;
import com.hit.joonggonara.repository.MemberRepository;
import com.hit.joonggonara.type.LoginType;
import com.hit.joonggonara.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchRuntimeException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;
    
    
    @Test
    @DisplayName("[Service] 회원 정보 존재")
    void userExistTest() throws Exception
    {
        //given
        Member member = createMember();
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername(member.getEmail());
        //then
        assertThat(userDetails.getUsername()).isEqualTo(member.getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(member.getPassword());
        then(memberRepository).should().findByEmail(any());
    }
    
    @Test
    @DisplayName("[Service] 존재하지 않은 회원")
    void NotFoundUserTest() throws Exception
    {
        //given
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());
        //when
        CustomException exception = (CustomException) catchRuntimeException(()->userDetailsService.loadUserByUsername("test"));
        //then
        assertThat(exception.getErrorCode().getHttpStatus()).isEqualTo(UserErrorCode.USER_NOT_FOUND.getHttpStatus());
        assertThat(exception).hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());
        then(memberRepository).should().findByEmail(any());
    }

    private Member createMember() {
        return Member.builder()
                .email("test@naver.com")
                .name("홍길동")
                .password("abc1234")
                .nickName("nickName")
                .phoneNumber("010-1234-4567")
                .school("school")
                .loginType(LoginType.GENERAL)
                .role(Role.USER)
                .build();
    }

}