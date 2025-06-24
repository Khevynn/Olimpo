package com.olimpo.Services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.olimpo.Entity.UserEntity;

/**
 * Service responsible for JWT token management.
 * Handles token generation, validation, and expiration.
 */
@Service
public class TokenService {

    // Configuration
    @Value("${api.security.token.access-secret}")
    private String accessSecret;

    @Value("${api.security.token.refresh-secret}")
    private String refreshSecret;

    // Constants
    private static final String TOKEN_ISSUER = "Olimpo API";

    /**
     * Validates a refresh token and returns the user's email if valid.
     * @param refreshToken The refresh token to validate
     * @return The user's email if the token is valid, null otherwise
     */
    public String validateRefreshToken(String refreshToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(refreshSecret);
            var verifier = JWT.require(algorithm).build();
            var decodedJWT = verifier.verify(refreshToken);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    /**
     * Validates an access token and returns the user's email if valid.
     * @param token The access token to validate
     * @return The user's email if the token is valid, null otherwise
     */
    public String validateAccessToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(accessSecret);
            return JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    /**
     * Generates a new access token for a user.
     * @param user The user to generate the token for
     * @return The generated access token
     * @throws RuntimeException if token generation fails
     */
    public String generateAccessToken(UserEntity user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(accessSecret);
            return JWT.create()
                .withIssuer(TOKEN_ISSUER)
                .withSubject(user.getEmail())
                .withExpiresAt(getAccessTokenExpiration())
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating access token", e);
        }
    }

    /**
     * Generates a new refresh token for a user.
     * @param user The user to generate the token for
     * @return The generated refresh token
     * @throws RuntimeException if token generation fails
     */
    public String generateRefreshToken(UserEntity user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(refreshSecret);
            return JWT.create()
                .withIssuer(TOKEN_ISSUER)
                .withSubject(user.getEmail())
                .withExpiresAt(getRefreshTokenExpiration())
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating refresh token", e);
        }
    }

    // Private Helper Methods

    /**
     * Gets the expiration time for access tokens.
     * @return The expiration time (1 minute from now)
     */
    private Instant getAccessTokenExpiration() {
        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("+01:00"));
    }

    /**
     * Gets the expiration time for refresh tokens.
     * @return The expiration time (5 days from now)
     */
    private Instant getRefreshTokenExpiration() {
        return LocalDateTime.now().plusDays(5).toInstant(ZoneOffset.of("+01:00"));
    }
}