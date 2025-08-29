package org.cobee.server.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.auth.jwt.JwtTokenProvider;
import org.cobee.server.auth.jwt.TokenInfo;
import org.cobee.server.auth.util.CookieUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static org.cobee.server.auth.handler.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final ObjectMapper objectMapper;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.objectMapper = objectMapper;
        setDefaultTargetUrl("http://localhost:8080/home");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(jakarta.servlet.http.Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // API 테스트를 위한 처리
        if (targetUrl.contains("mode=api")) {
            response.setContentType("application/json;charset=UTF-8");
            try {
                response.getWriter().write(objectMapper.writeValueAsString(tokenInfo));
                return null; // 리다이렉트하지 않음
            } catch (IOException e) {
                log.error("Failed to write token info to response", e);
                return targetUrl; // 에러시 기본 리다이렉트
            }
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", tokenInfo.getAccessToken())
                .queryParam("refresh_token", tokenInfo.getRefreshToken())
                .queryParam("expiresIn", tokenInfo.getRefreshTokenExpirationTime())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}