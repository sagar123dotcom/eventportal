package com.community.events.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    public static final String SESSION_KEY = "adminUser";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if (req.getSession().getAttribute(SESSION_KEY) == null) {
            res.sendRedirect(req.getContextPath() + "/admin/login");
            return false;
        }
        return true;
    }
}
