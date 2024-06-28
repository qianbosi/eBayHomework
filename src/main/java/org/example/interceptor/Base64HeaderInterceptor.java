package org.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

@Component
public class Base64HeaderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("X-Role-Info");
        if (header == null || !header.startsWith("Basic ")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Missing or invalid role info header.");
            return false;
//            throw new ServletException("Missing or invalid role info header.");
        }
        String base64RoleInfo = header.substring(6).trim();  //The length of "Basic " is 6
        String roleInfo = new String(Base64.getDecoder().decode(base64RoleInfo), "UTF-8");
        String[] values = roleInfo.split(":");
        if (values.length != 3) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Format of role info is invalid.");
//            throw new ServletException("Format of role info is invalid");
            return false;
        }
        // Assuming the role info are in the format "userId:accountName:role"
        String userId = values[0];
        String accountName = values[1];
        String role = values[2];
        request.setAttribute("userId", userId);
        request.setAttribute("accountName", accountName);
        request.setAttribute("role", role);
        return true;
    }
}