package com.SuaraCloud.UserService;

import com.SuaraCloud.UserService.model.TokenValidationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // Call auth-service to validate token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TokenValidationResponse> validationResponse =
                    restTemplate.exchange("http://authservice/auth/validate",
                            HttpMethod.POST,
                            entity,
                            TokenValidationResponse.class);

            if (validationResponse.getBody() != null && validationResponse.getBody().isValid()) {
                // Store token ID in request attribute for later use
                request.setAttribute("tokenId", validationResponse.getBody().getUserId());
                return true;
            }
        } catch (Exception e) {
            // Log error
            System.err.println("Error validating token: " + e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}