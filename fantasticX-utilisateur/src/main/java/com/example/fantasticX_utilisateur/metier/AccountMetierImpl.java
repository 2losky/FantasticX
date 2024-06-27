package com.example.fantasticX_utilisateur.metier;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fantasticX_utilisateur.dao.AppRoleRepository;
import com.example.fantasticX_utilisateur.dao.AppUserRepository;
import com.example.fantasticX_utilisateur.entity.AppRole;
import com.example.fantasticX_utilisateur.entity.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountMetierImpl implements AccountMetier {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountMetierImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) throws Exception {
        if (appUser == null) throw new Exception("User cannot be null");
        String password = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(password));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) throws Exception {
        if (appRole == null) throw new Exception("Role cannot be null");
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) throws Exception {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appUser == null || appRole == null) throw new Exception("User or Role not found");
        appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser findUserByUsername(String username) throws Exception {
        if (username == null || username.isEmpty()) throw new Exception("Username cannot be null or empty");
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppRole findRoleByRoleName(String name) throws Exception {
        if (name == null || name.isEmpty()) throw new Exception("Role name cannot be null or empty");
        return appRoleRepository.findByRoleName(name);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public List<AppRole> listRoles() {
        return appRoleRepository.findAll();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationToken = request.getHeader(StaticConfigure.REQUEST_HEADER_AUTHORIZATION_KEY);
        if (authorizationToken != null && authorizationToken.startsWith(StaticConfigure.TOKEN_PREFIX)) {
            try {
                String jwt = authorizationToken.substring(StaticConfigure.TOKEN_PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(StaticConfigure.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();
                AppUser appUser = appUserRepository.findByUsername(username);
                String jwtAccessToken = JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + StaticConfigure.ACCESS_TOKEN_DURATION))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim(StaticConfigure.ROLES_CLAIMS_KEY, appUser.getAppRoles().stream().map(AppRole::getRoleName).collect(Collectors.toList()))
                        .sign(algorithm);
                StaticConfigure.responseToMap(response, jwtAccessToken, jwt);
            } catch (Exception e) {
                response.setHeader(StaticConfigure.ERROR_MESSAGE_KEY, e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException(StaticConfigure.REFRESH_TOKEN_RIQUIRED_ERROR_MESSAGE);
        }
    }

    @Override
    public AppUser profile(Principal principal) {
        return appUserRepository.findByUsername(principal.getName());
    }
}
