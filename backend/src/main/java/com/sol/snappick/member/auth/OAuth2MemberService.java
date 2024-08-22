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
        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println("====================================");
        for (String key : attributes.keySet()) {
            System.out.println(key + ": " + attributes.get(key));
        }
        System.out.println("====================================");

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

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
