package com.olimpo.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.DTO.Responses.Auth.LoginResponse;

public class ResponseUtils {
    // Success Responses
    public static ResponseEntity<APIResponse> created(String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new APIResponse(message));
    }

    public static ResponseEntity<APIResponse> ok(String message) {
        return ResponseEntity.ok(new APIResponse(message));
    }

    public static ResponseEntity<APIResponse> loginSuccess(String token) {
        return ResponseEntity.ok(new LoginResponse(token, "Login bem-sucedido"));
    }

    // Error Responses
    public static ResponseEntity<APIResponse> badRequest(String message) {
        return ResponseEntity.badRequest()
            .body(new APIResponse(message));
    }

    public static ResponseEntity<APIResponse> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new APIResponse(message));
    }

    public static ResponseEntity<APIResponse> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new APIResponse(message));
    }

    public static ResponseEntity<APIResponse> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new APIResponse(message));
    }

    public static ResponseEntity<APIResponse> conflict(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new APIResponse(message));
    }

    public static ResponseEntity<APIResponse> serverError(String message) {
        return ResponseEntity.internalServerError()
            .body(new APIResponse(message));
    }

    // Token Responses
    public static ResponseEntity<APIResponse> invalidToken() {
        return unauthorized(INVALID_TOKEN);
    }

    public static ResponseEntity<APIResponse> expiredToken() {
        return unauthorized(TOKEN_EXPIRED);
    }

    public static ResponseEntity<APIResponse> missingToken() {
        return unauthorized(MISSING_TOKEN);
    }

    // Common Error Messages
    public static final String INTERNAL_SERVER_ERROR = "Erro interno do servidor. Tente novamente mais tarde.";
    public static final String INVALID_CREDENTIALS = "E-mail ou senha inválidos.";
    public static final String INVALID_TOKEN = "Token inválido ou expirado.";
    public static final String TOKEN_EXPIRED = "Token expirado. Por favor, faça login novamente.";
    public static final String MISSING_TOKEN = "Token não fornecido.";
    public static final String EMAIL_ALREADY_EXISTS = "E-mail já existe.";
    public static final String USER_NOT_FOUND = "Usuário não encontrado.";
    public static final String NO_TAGS_AVAILABLE = "Não há tags disponíveis para esse usuário, tente com outro usuário.";
    public static final String PASSWORDS_DONT_MATCH = "As senhas não coincidem.";
    public static final String INVALID_PASSWORD_FORMAT = "A senha deve conter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula e um número.";
    public static final String INVALID_TAG_LENGTH = "Tag precisa ter 5 caracteres ou menos.";
    public static final String INVALID_TAG_FORMAT = "Tag deve conter apenas letras e números.";
    public static final String USERNAME_TAG_IN_USE = "Nome de usuário e tag já em uso.";
    public static final String ACCOUNT_BANNED_OR_DELETED = "Conta excluída ou banida!";
} 