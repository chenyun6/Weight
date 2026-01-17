package com.cy.adapter.weight.web;

import com.cy.app.weight.auth.AuthService;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 认证拦截器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private AuthService authService;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String USER_ID = "userId";

    private static final Set<String> SKIP_AUTH_API_SET = Sets.newHashSet("/send-code", "/login", "/refresh-token");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 排除登录和发送验证码接口
        String path = request.getRequestURI();
        if (SKIP_AUTH_API_SET.contains(path)) {
            return true;
        }

        // 获取Token
        String token = getTokenFromRequest(request);
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 验证Token
        Long userId = authService.validateAndGetUserId(token);
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 将用户ID存储到request中
        request.setAttribute(USER_ID, userId);
        return true;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
