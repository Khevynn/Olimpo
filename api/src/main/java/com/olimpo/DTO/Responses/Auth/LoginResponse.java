package com.olimpo.DTO.Responses.Auth;

import com.olimpo.DTO.Responses.APIResponse;

import lombok.Getter;

@Getter
public class LoginResponse extends APIResponse {
    private String token;

    public LoginResponse(String message) {
        super(message);
        this.token = null;
    }

    public LoginResponse(String token, String message) {
        super(message);
        this.token = token;
    }
} 