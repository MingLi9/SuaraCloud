package com.SuaraCloud.SongMetaService;

import com.SuaraCloud.SongMetaService.model.TokenValidationResponse;
import com.SuaraCloud.SongMetaService.model.UserValidationResponse;
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

    private static final String AUTH_SERVICE_URL = "http://authservice/auth/validate";
    private static final String USER_SERVICE_URL_TEMPLATE = "http://userservice/users/token/%s";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (!isValidAuthHeader(authHeader)) {
            System.err.println("Invalid auth header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            System.err.println("Error validating token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String validatedUserId = validateUser(userId, authHeader);
        if (validatedUserId == null) {
            System.err.println("Error validating user");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        request.setAttribute("userId", validatedUserId);
        return true;
    }

    private boolean isValidAuthHeader(String header) {
        return header != null && header.startsWith("Bearer ");
    }

    private HttpEntity<String> createAuthEntity(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return new HttpEntity<>(headers);
    }

    private String validateTokenAndGetUserId(String authHeader) {
        try {
            ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(
                    AUTH_SERVICE_URL,
                    HttpMethod.POST,
                    createAuthEntity(authHeader),
                    TokenValidationResponse.class
            );
            TokenValidationResponse body = response.getBody();
            return (body != null && body.isValid()) ? body.getUserId() : null;
        } catch (Exception e) {
            System.err.println("Error validating token: " + e.getMessage());
            return null;
        }
    }

    private String validateUser(String userId, String authHeader) {
        try {
            String url = String.format(USER_SERVICE_URL_TEMPLATE, userId);
            ResponseEntity<UserValidationResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createAuthEntity(authHeader),
                    UserValidationResponse.class
            );
            UserValidationResponse body = response.getBody();
            return (body != null) ? body.getId() : null;
        } catch (Exception e) {
            System.err.println("Error validating user internally: " + e.getMessage());
            return null;
        }
    }
}