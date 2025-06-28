package com.olimpo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.olimpo.Routes.APIRoutes;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // the browser sends an OPTIONS before anything

                        .requestMatchers(HttpMethod.POST, APIRoutes.REFRESH_TOKEN_ROUTE).permitAll()
                        .requestMatchers(HttpMethod.POST, APIRoutes.USER_LOGIN_ROUTE).permitAll()
                        .requestMatchers(HttpMethod.POST, APIRoutes.USER_REGISTER_ROUTE).permitAll()
                        .requestMatchers(HttpMethod.GET, APIRoutes.USER_LOGOUT_ROUTE).authenticated()

                        .requestMatchers(APIRoutes.USER_GET_PROFILE_ROUTE).authenticated()
                        .requestMatchers(APIRoutes.USER_UPDATE_PROFILE_ROUTE).authenticated()
                        .requestMatchers(APIRoutes.USER_DELETE_ROUTE).authenticated()

                        .requestMatchers(APIRoutes.FRIENDS_GET_ALL).authenticated()
                        .requestMatchers(APIRoutes.FRIENDS_DELETE).authenticated()
                        .requestMatchers(APIRoutes.FRIENDS_SEND).authenticated()
                        .requestMatchers(APIRoutes.FRIENDS_RESPOND).authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
