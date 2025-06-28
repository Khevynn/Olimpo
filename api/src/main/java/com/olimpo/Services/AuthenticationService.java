package com.olimpo.Services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.olimpo.DTO.Requests.Auth.LoginRequestDTO;
import com.olimpo.DTO.Requests.Auth.RegisterRequestDTO;
import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.Entity.UserEntity;
import com.olimpo.Enums.AccountStatus;
import com.olimpo.Repository.UserRepository;
import com.olimpo.Utils.PasswordUtils;
import com.olimpo.Utils.ProfileUtils;
import com.olimpo.Utils.ResponseUtils;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {
    // Dependencies
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    // Constants
    private static final int REFRESH_TOKEN_MAX_AGE = 5 * 24 * 60 * 60; // 5 days in seconds
    private static final String REFRESH_TOKEN_PATH = "/auth/refresh";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    // Public Methods
    public ResponseEntity<?> refreshToken(String refreshToken, HttpServletResponse response) {
        try {
            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseUtils.missingToken();
            }

            String userEmail = tokenService.validateRefreshToken(refreshToken);
            if (userEmail == null) {
                return ResponseUtils.invalidToken();
            }

            UserEntity user = ProfileUtils.getUserOrThrow(userRepository, userEmail);
            String accessToken = generateAccessAndRefreshTokens(user, response);

            return ResponseEntity.ok().body(Map.of("accessToken", accessToken));
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseUtils.notFound(e.getMessage());
            }
            throw e;
        }
    }

    public ResponseEntity<APIResponse> login(LoginRequestDTO request, HttpServletResponse response) {
        try {
            UserEntity user = ProfileUtils.getUserOrThrow(userRepository, request.getEmail());

            if (!PasswordUtils.passwordMatches(request.getPassword(), user.getPassword())) {
                return ResponseUtils.unauthorized(ResponseUtils.INVALID_CREDENTIALS);
            }

            if (ProfileUtils.isAccountBannedOrDeleted(user)) {
                return ResponseUtils.forbidden(ResponseUtils.ACCOUNT_BANNED_OR_DELETED);
            }

            String accessToken = generateAccessAndRefreshTokens(user, response);
            return ResponseUtils.loginSuccess(accessToken);

        } catch(ResponseStatusException e){
            System.out.println(e.getMessage());
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseUtils.unauthorized(ResponseUtils.INVALID_CREDENTIALS);
            }
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return handleLoginError(e);
        }
    }

    public ResponseEntity<APIResponse> logout(String refreshToken, HttpServletResponse response) {
        try {
            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseUtils.missingToken();
            }

            String userEmail = tokenService.validateRefreshToken(refreshToken);
            if (userEmail == null) {
                return ResponseUtils.invalidToken();
            }

            UserEntity user = ProfileUtils.getUserOrThrow(userRepository, userEmail);
            clearUserRefreshToken(user);
            sendRefreshTokenCookie(response, "");

            return ResponseUtils.ok("Logout successful.");
        } catch(ResponseStatusException e){
            System.out.println(e.getMessage());
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseUtils.notFound(e.getMessage());
            }
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return handleLogoutError(e);
        }
    }

    public ResponseEntity<APIResponse> register(RegisterRequestDTO request) {
        try {
            if (isEmailAlreadyRegistered(request.getEmail())) {
                return ResponseUtils.badRequest(ResponseUtils.EMAIL_ALREADY_EXISTS);
            }
            
            if (!PasswordUtils.isPasswordStrong(request.getPassword())) {
                return ResponseUtils.badRequest(ResponseUtils.INVALID_PASSWORD_FORMAT);
            }

            String tag = generateUserTag(request.getUser());
            if (tag == null) {
                return ResponseUtils.conflict(ResponseUtils.NO_TAGS_AVAILABLE);
            }

            UserEntity user = createNewUser(request, tag);
            return ResponseUtils.created("Usuário cadastrado com sucesso.");

        } catch(ResponseStatusException e){
            System.out.println(e.getMessage());
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseUtils.notFound(e.getMessage());
            }
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return handleRegistrationError(e);
        }
    }

    // Private Methods - Token Management
    private String generateAccessAndRefreshTokens(UserEntity user, HttpServletResponse response) {
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        sendRefreshTokenCookie(response, refreshToken);

        return accessToken;
    }

    private void sendRefreshTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
            .httpOnly(true)
            .secure(false)
            .path(REFRESH_TOKEN_PATH)
            .maxAge(REFRESH_TOKEN_MAX_AGE)
            .sameSite("Strict")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // Private Methods - User Management
    private boolean isEmailAlreadyRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private String generateUserTag(String username) {
        return ProfileUtils.generateUniqueTag(userRepository.findAllByUsername(username));
    }

    private UserEntity createNewUser(RegisterRequestDTO request, String tag) {
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUser());
        user.setTag(tag);
        user.setPassword(PasswordUtils.getEncodedPassword(request.getPassword()));
        user.setAccountStatus(AccountStatus.Activated);
        return userRepository.save(user);
    }

    private void clearUserRefreshToken(UserEntity user) {
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    // Private Methods - Error Handling
    private ResponseEntity<APIResponse> handleLoginError(Exception e) {
        System.out.println(e.getMessage());
        return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<APIResponse> handleLogoutError(Exception e) {
        System.out.println(e.getMessage());
        return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<APIResponse> handleRegistrationError(Exception e) {
        System.out.println(e.getMessage());
        return ResponseUtils.serverError("Internal server error while creating user.");
    }
} 