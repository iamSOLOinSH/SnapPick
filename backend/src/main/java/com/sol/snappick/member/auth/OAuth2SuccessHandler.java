package com.sol.snappick.member.auth;

import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getName();

        log.info("authentication : {}", authentication);
        log.info("authentication principal: {}", authentication.getPrincipal());

        Member member = memberRepository.findByEmail((String) oAuth2User.getAttribute("email"))
                .orElseThrow(
                        () -> new IllegalArgumentException("Unexpected user"));

        String accessToken = tokenService.generateToken(member, ACCESS_TOKEN_DURATION);

        if (member.getCreatedAt() == null) { // 회원가입이라면
            memberRepository.save(member);

            response.sendRedirect(
                    "http://localhost:8081/signup-success.html?name="
                            + URLEncoder.encode(
                            member.getName(),
                            StandardCharsets.UTF_8.toString())
                            + "&token=" + accessToken);
        } else {
            response.sendRedirect(
                    "http://localhost:8081/login-success.html?name="
                            + URLEncoder.encode(
                            member.getName(),
                            StandardCharsets.UTF_8.toString())
                            + "&token=" + accessToken);
        }
    }
}
