package com.example.demo.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInterceptor implements HandlerInterceptor {

    // 요청 전에 실행되는 메서드
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 세션에서 로그인 정보를 확인
        Object user = request.getSession().getAttribute("username");

        // 로그인 정보가 없으면 로그인 페이지로 리다이렉트
        if (user == null) {
            response.sendRedirect("/user/login");
            return false; // 요청을 중단
        }

        // 로그인 정보가 있으면 요청을 진행
        return true;
    }
}
