package com.olimpo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.olimpo.DTO.Requests.Auth.LoginRequestDTO;
import com.olimpo.DTO.Requests.Auth.RegisterRequestDTO;
import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.Routes.APIRoutes;
import com.olimpo.Services.AuthenticationService;
import com.olimpo.Utils.ResponseUtils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping(APIRoutes.VERIFY_TOKEN_ROUTE)
    public ResponseEntity<APIResponse> verifyToken() {
        return ResponseUtils.ok("Token verified successfully.");
    }

    @PostMapping(APIRoutes.USER_LOGIN_ROUTE)
    public ResponseEntity<APIResponse> login(@RequestBody @Valid LoginRequestDTO request, HttpServletResponse response) {
        return authenticationService.login(request, response);
    }

    @PostMapping(APIRoutes.USER_REGISTER_ROUTE)
    public ResponseEntity<APIResponse> register(@RequestBody @Valid RegisterRequestDTO request) {
        return authenticationService.register(request);
    }

    @PostMapping(APIRoutes.USER_LOGOUT_ROUTE)
    public ResponseEntity<APIResponse> logout(@RequestBody String refreshToken, HttpServletResponse response) {
        return authenticationService.logout(refreshToken, response);
    }

    @PostMapping(APIRoutes.REFRESH_TOKEN_ROUTE)
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        return authenticationService.refreshToken(refreshToken, response);
    }
} 