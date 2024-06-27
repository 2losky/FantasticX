package com.example.fantasticX_utilisateur.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticConfigure {

    public static final String SECRET = "K0n@n1007&675IT1nG&&NeR";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String REQUEST_HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final long ACCESS_TOKEN_DURATION = 5*60*1000;
    public static final long REFRESH_TOKEN_DURATION = 3*60*1000;
    public static final String ROLES_CLAIMS_KEY = "roles";
    public static final String ERROR_MESSAGE_KEY = "error-message";
    public static final String ACCESS_TOKEN_KEY = "access-token";
    public static final String REFRESH_TOKEN_KEY = "refresh-token";
    public static final String APPLICATION_CONTENT_TYPE = "application/json";
    public static final String REFRESH_TOKEN_RIQUIRED_ERROR_MESSAGE = "Refresh token required";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static String generateAccessToken(User user, HttpServletRequest request, Algorithm algorithm, List<?> authorities){
        String generatedToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_DURATION))
                .withIssuer(request.getRequestURI().toString())
                .withClaim(ROLES_CLAIMS_KEY, authorities)
                .sign(algorithm);
        return generatedToken;
    }

    public static String generateRefreshToken(User user, HttpServletRequest request, Algorithm algorithm){
        String jwtRefreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_DURATION))
                .withIssuer(request.getRequestURI().toString())
                .sign(algorithm);
        return jwtRefreshToken;
    }

    public static void responseToMap(HttpServletResponse response, String jwtAccessToken, String jwtRefreshToken) throws IOException {
        Map<String, String> idToken = new HashMap<>();
        idToken.put(StaticConfigure.ACCESS_TOKEN_KEY, jwtAccessToken);
        idToken.put(StaticConfigure.REFRESH_TOKEN_KEY, jwtRefreshToken);
        response.setContentType(StaticConfigure.APPLICATION_CONTENT_TYPE);
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }


}
