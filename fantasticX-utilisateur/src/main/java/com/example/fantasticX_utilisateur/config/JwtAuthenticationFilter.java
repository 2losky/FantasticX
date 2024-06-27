package com.example.fantasticX_utilisateur.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.fantasticX_utilisateur.utils.StaticConfigure;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter(StaticConfigure.USERNAME);
        String password = request.getParameter(StaticConfigure.PASSWORD);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            User user = (User) authResult.getPrincipal();
            Algorithm algorithm = Algorithm.HMAC256(StaticConfigure.SECRET);
            List<String> authoritiesList = user.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.toList());
            String jwtAccessToken = StaticConfigure.generateAccessToken(user, request, algorithm, authoritiesList);
            String jwtRefreshToken = StaticConfigure.generateRefreshToken(user, request, algorithm);
            StaticConfigure.responseToMap(response, jwtAccessToken, jwtRefreshToken);
        } catch (Exception e) {
            response.setHeader(StaticConfigure.ERROR_MESSAGE_KEY, e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
