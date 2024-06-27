package com.example.fantasticX_utilisateur.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fantasticX_utilisateur.utils.StaticConfigure;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/refreshToken")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationToken = request.getHeader(StaticConfigure.REQUEST_HEADER_AUTHORIZATION_KEY);
        if (authorizationToken != null && authorizationToken.startsWith(StaticConfigure.TOKEN_PREFIX)) {
            try {
                String jwt = authorizationToken.substring(StaticConfigure.TOKEN_PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(StaticConfigure.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim(StaticConfigure.ROLES_CLAIMS_KEY).asArray(String.class);
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                for (String role : roles) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                    authorities.add(grantedAuthority);
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                response.setHeader(StaticConfigure.ERROR_MESSAGE_KEY, e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                e.printStackTrace();
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
