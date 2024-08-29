package com.sol.snappick.member.auth;

import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static com.sol.snappick.member.auth.JwtConfig.ACCESS_TOKEN_DURATION;

/**
 * OAuth2 인증 완료 시 동작하는 SuccessHandler
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @Value("${frontend.url}")
    private String frontendUrl;


    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) ((Map<String, Object>) (oAuth2User.getAttributes()).get("kakao_account")).get("email");

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalStateException("An error has occurred")
        );

        String accessToken = tokenService.generateToken(member, ACCESS_TOKEN_DURATION);
        CookieUtils.addCookie(response, "token", accessToken,
                (int) ACCESS_TOKEN_DURATION.toSeconds());

        if (member.getUserKey() == null) {
            // 회원가입에 폼정보 입력 안된 사람이라면
            getRedirectStrategy().sendRedirect(request, response, frontendUrl + "/signup");
        } else {
            getRedirectStrategy().sendRedirect(request, response, frontendUrl + "/home");
        }
    }
}
