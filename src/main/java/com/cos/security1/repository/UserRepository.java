package com.cos.security1.repository;

import com.cos.security1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository 가 없어도 IoC가 됩니다. JpaRepository를 상속받았기 떄문
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
