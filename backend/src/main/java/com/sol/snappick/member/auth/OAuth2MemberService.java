package com.sol.snappick.member.auth;

import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        registerMember(user);
        return user;
    }

    // 회원가입
    private Member registerMember(OAuth2User oAuth2User) {
        Map<String, Object> kakao_account =
                ((Map<String, Object>) (((Map<String, Object>) oAuth2User.getAttributes()).get("kakao_account")));
        String email = (String) kakao_account.get("email");
        String name = (String) ((Map<String, Object>) kakao_account.get("profile")).get("nickname");

        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent()) {
            return member.get();
        }
        return memberRepository.save(
                Member.builder()
                        .email(email)
                        .name(name)
                        .build());
    }
}
