package com.cos.security1.config.auth.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.auth.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.auth.oauth.provider.NaverUserInfo;
import com.cos.security1.config.auth.oauth.provider.OAuth2UserInfo;
import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션 만들어진다
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글 로그인 버튼 클릭 -> 로그인 창 -> 로그인 완료 -> code를 리턴 (OAuth-client라이브러리) -> Access Token요청
        //UserRequest 정보-> loadUser함수 호출-> 구글로부터 회원 프로필 받아준다
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }

        String provider = oAuth2UserInfo.getProvider(); //google
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerId; //google_123451687545465
        String password = passwordEncoder.encode(username);
        String email = oAuth2UserInfo.getEmail();
        String role = "USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            System.out.println("구글 로그인 최초 입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .providerId(providerId)
                    .provider(provider)
                    .role(role)
                    .build();

            userRepository.save(userEntity);
        }
        if(userEntity != null) {
            System.out.println("이미 구글 계정으로 회원가입 실시 완료");
        }

        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
    }
}
