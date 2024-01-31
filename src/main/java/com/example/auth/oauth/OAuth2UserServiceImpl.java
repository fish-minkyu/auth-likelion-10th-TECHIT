package com.example.auth.oauth;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// 소셜 로그인을 성공했을 때, 어떤 식으로 처리할지 결정
@Slf4j
@Service // 꼭 붙일 필요는 없는데 명시적으로 써준다.
public class OAuth2UserServiceImpl
  // 기본적인 OAuth2 인증 과정을 진행해주는 클래스
  extends DefaultOAuth2UserService {

  @Override
  // 사용자 정보 받아오기
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    // 어떤 서비스 제공자를 사용했는지
    String registrationId = userRequest
      .getClientRegistration()
      .getRegistrationId();
    //TODO 서비스 제공자에 따라 데이터 처리를 달리 하고 싶을 때

    // OAuth2 제공자로부터 받은 데이터를 원하는 방식으로 다시 정리하기 위한 Map
    Map<String, Object> attributes = new HashMap<>();
    String nameAttribute = "";

    // Kakao 아이디로 로그인
    if (registrationId.equals("kakao")) {
      log.info(oAuth2User.getAttributes().toString());
      // Kakao에서 받아온 정보다.
      attributes.put("provider", "kakao");
      attributes.put("id", oAuth2User.getAttribute("id"));
      Map<String, Object> kakaoAccount
        = oAuth2User.getAttribute("kakao_account");
      attributes.put("email", kakaoAccount.get("email"));

      Map<String, Object> kakaoProfile
        = (Map<String, Object>) kakaoAccount.get("profile");
      attributes.put("nickname", kakaoProfile.get("nickname"));
      attributes.put("profileImg", kakaoProfile.get("profile_image_url"));
      nameAttribute = "email";
    }

    // Naver 아이디로 로그인
    if (registrationId.equals("naver")) {
      // Naver에서 받아온 정보다.
      attributes.put("provider", "naver");

      Map<String, Object> responseMap
        // 네이버가 반환한 JSON에서 response를 회수
        = oAuth2User.getAttribute("response");
      attributes.put("id", responseMap.get("id")); // 고유 식별자, PK
      attributes.put("email", responseMap.get("email"));
      attributes.put("nickname", responseMap.get("nickname"));
      attributes.put("name", responseMap.get("name"));
      attributes.put("profileImg", responseMap.get("profile_image"));
      nameAttribute = "email"; // 어떤 애를 이름으로 취급할지 써야 한다.
    }
    log.info(attributes.toString());
    return new DefaultOAuth2User(
      Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 권한 설정
      attributes, // 위에서 정리한 정보
      nameAttribute
    );
  }
}
