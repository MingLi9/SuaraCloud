package com.SuaraCloud.AuthService.Controller;

import com.SuaraCloud.AuthService.Model.TokenValidationResponse;
import com.SuaraCloud.AuthService.Service.JwtValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtValidationService jwtValidationService;

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(new TokenValidationResponse(false, null));
        }

        String token = authHeader.substring(7);
        try {
            String userId = jwtValidationService.extractUserIdFromToken(token);
            return ResponseEntity.ok(new TokenValidationResponse(true, userId));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new TokenValidationResponse(false, null));
        }
    }
}
