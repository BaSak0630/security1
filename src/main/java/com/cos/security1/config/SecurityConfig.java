package com.cos.security1.config;

import com.cos.security1.config.auth.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//구글 로그인이 완료된 뒤 후처리 필요 1.코드 받기(인증) 2. 액세스 토큰(권한)
// 3.사용자 프로필 정보 가져오기 4-1.그 정보를 토대로 회원가입 진행 가능
// 4-2. 구글에서 재공하는 정보 말고 우리 비즈니스에 맞는 정보를 추가 해줘야합니다.
@Configuration
@EnableWebSecurity //활성화 -> 스프링 시큐리티 필터가 스프링 필터 체인에 등록됨
public class SecurityConfig {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeRequests ->authorizeRequests
                                .requestMatchers("/user/**").authenticated()
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                .anyRequest().permitAll()
                ).formLogin(formLogin ->{
                    formLogin.loginPage("/loginForm")
                            .usernameParameter("username") //요처하는 파라메터가 달라지면 여기서 설정
                            .loginProcessingUrl("/login")// /login 주소가 호출이 되면 시큘리티가 대신 로그인 진행
                            .defaultSuccessUrl("/");
                }).oauth2Login(oauth2Login ->{
                    oauth2Login.loginPage("/oauth2/authorization/google")//Tip. 코드X (엑세스 토큰 + 사용자 프로칠 정보 O)
                            .defaultSuccessUrl("/")
                            .failureUrl("/oauth2/authorization/google")
                            .userInfoEndpoint(userInfoEndpoint ->
                                    userInfoEndpoint.userService(principalOauth2UserService)
                            );
                });

        return http.build();
    }
}
