package com.cos.security1.config.auth;

import com.cos.security1.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행 시킨다.
//로그인을 진행이 완료가 되면 시큐리티 session 을 만들어 줍니다. (Security ContextHolder)
//오브젝트 타입 -> Authentication 타입 객체
//Authentication 안에 User 정보가 있어야 함
//User 오브젝트타입 -> UserDetails 다입 객체

//Security Session -> Authentication -> UserDetails(PrincipalDetails)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; //콜 포지션
    private Map<String, Object> attributes = new HashMap<>();

    //일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }
    //OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //OAuth2User
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public String getName() {
        return null;//별로 중요하지 않음
    }

    //UserDetails
    //해당 User 의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //authorities.add(new SimpleGrantedAuthority(user.getRole()));
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정 만료
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠금
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 오랜 기간사용시
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //활성화
    @Override
    public boolean isEnabled() {
        return true;
    }
}
