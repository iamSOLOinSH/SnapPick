package com.sol.snappick.member.auth;

import com.sol.snappick.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final OAuth2MemberService oAuth2MemberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);


        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(tokenAuthenticationFilter(),
                RequestCacheAwareFilter.class);

        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(new AntPathRequestMatcher("/token")
                                , new AntPathRequestMatcher("/index.html")
                                , new AntPathRequestMatcher("/**"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/**"))
                        .authenticated()
                        .anyRequest()
                        .permitAll());

        http.oauth2Login(
                (oauth2) -> {
                    oauth2
                            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                    .userService(oAuth2MemberService))
                            .authorizationEndpoint(
                                    authorization -> // http://localhost:8080/oauth2/authorize/google
                                            authorization.baseUri("/oauth2/authorization/kakao"))
//                    .redirectionEndpoint(redirection ->
//                        redirection.baseUri("/login/oauth2/code/*"))
                            .successHandler(oAuth2SuccessHandler());
                }
        );

        http.exceptionHandling(
                exceptionConfig -> exceptionConfig
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/**"))
        );

        return http.build();

    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(
                tokenService);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(memberRepository, tokenService); // 여기 뭐넣지
    }

}
