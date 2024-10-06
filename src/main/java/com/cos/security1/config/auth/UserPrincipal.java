package com.cos.security1.config.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(com.cos.security1.domain.User user) {
        super(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole())));
        //DB에서 사용자마다 권한이 다르면 적용해주어야함
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
