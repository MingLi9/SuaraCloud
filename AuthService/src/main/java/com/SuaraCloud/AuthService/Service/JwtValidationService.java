package com.SuaraCloud.AuthService.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
@Service
public class JwtValidationService {

    @Value("${supabase.jwt.secret}")
    private String jwtSecret;

    public DecodedJWT validateToken(String token) {
        try {
            // Print the current time for debugging
            Date now = new Date();

            // Decode without verification first to inspect the token
            DecodedJWT unverified = JWT.decode(token);

            // Create the algorithm with the correct secret
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

            // Build a more lenient verifier
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("https://hxuegtmworlasjwdqsya.supabase.co/auth/v1")
                    // Don't check the expiration for now
                    .acceptLeeway(5 * 60) // 5 minutes leeway for clock skew
                    .build();

            DecodedJWT verified = verifier.verify(token);
            return verified;
        } catch (JWTVerificationException exception) {
            System.out.println("JWT Verification failed: " + exception.getMessage());

            // Try to decode the token without verification to get more info
            try {
                DecodedJWT jwt = JWT.decode(token);
                System.out.println("Token issuer: " + jwt.getIssuer());
                System.out.println("Token subject: " + jwt.getSubject());
                System.out.println("Token expiration: " + jwt.getExpiresAt());
                System.out.println("Token issued at: " + jwt.getIssuedAt());
            } catch (Exception e) {
                System.out.println("Could not decode token: " + e.getMessage());
            }

            throw new RuntimeException("Invalid JWT token", exception);
        }
    }

    public String extractUserIdFromToken(String token) {
        try {
            // For now, just decode without verification to get the subject
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (Exception e) {
            System.out.println("Error extracting user ID: " + e.getMessage());
            throw new RuntimeException("Error extracting user ID", e);
        }
    }
}