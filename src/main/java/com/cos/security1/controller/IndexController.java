package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { //2가지 방법 가능
        System.out.println("/test/login ===========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication = " + principalDetails.getUser());

        System.out.println("userDetails = " + userDetails.getUser());
        return "세션 정보 확인 하기";
    }
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth) { //2가지 방법 가능
        System.out.println("/test/oauth/login ===========");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication = " + oAuth2User.getAttributes());
        System.out.println("oAuth2User = " + oAuth.getAttributes());
        return "OAuth 세션 정보 확인 하기";
    }

    @GetMapping({"","/"})
    public String index() {
        return "index";
    }

    @GetMapping({"/user"})
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {// /test참고
        System.out.println("principal details = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping({"/admin"})
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping({"/manager"})
    public @ResponseBody String manager() {
        return "manager";
    }

    //SecurityConfig 파일 생성 후 기존 제공하는 login 작동 X
    @GetMapping({"/loginForm"})
    public String login() {
        return "loginForm";
    }

    @GetMapping({"/joinForm"})
    public String joinForm() {
        return "joinForm";
    }
    @PostMapping({"/join"})
    public @ResponseBody String join(User user) {
        System.out.println(user);
        User user1 = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .role("USER")
                .build();

        userRepository.save(user1);
        return "redirect:/loginForm";
    }


    //@PreAuthorize: 메서드가 실행되기 전에 인증을 거친다.
    //@PostAuthorize: 메서드가 실행되고 나서 응답을 보내기 전에 인증을 거친다.
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인 정보";
    }
}
