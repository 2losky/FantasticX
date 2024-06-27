package com.example.fantasticX_utilisateur.metier;

import com.example.fantasticX_utilisateur.entity.AppRole;
import com.example.fantasticX_utilisateur.entity.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface AccountMetier {
    AppUser addNewUser(AppUser appUser) throws Exception;
    AppRole addNewRole(AppRole appRole) throws Exception;
    void addRoleToUser(String username, String roleName) throws Exception;
    AppUser findUserByUsername(String username) throws Exception;
    AppRole findRoleByRoleName(String roleName) throws Exception;
    List<AppUser> listUsers();
    List<AppRole> listRoles();
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    AppUser profile(Principal principal);

}
