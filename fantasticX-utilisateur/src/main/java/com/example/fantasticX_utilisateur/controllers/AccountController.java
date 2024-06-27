package com.example.fantasticX_utilisateur.controllers;

import com.example.fantasticX_utilisateur.entity.AppRole;
import com.example.fantasticX_utilisateur.entity.AppUser;
import com.example.fantasticX_utilisateur.metier.AccountMetier;
import com.example.fantasticX_utilisateur.utils.EndPointPath;
import com.example.fantasticX_utilisateur.utils.RoleUserForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(EndPointPath.ACCOUNT)
@CrossOrigin("*")
public class AccountController {

    private AccountMetier accountMetier;

    public AccountController(AccountMetier accountMetier){
        this.accountMetier = accountMetier;
    }


    @PostMapping(path = EndPointPath.ACCOUNT_SAVE_USER_PATH)
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppUser addNewUser(@RequestBody AppUser appUser) throws Exception {
        return accountMetier.addNewUser(appUser);
    }

    @PostMapping(path = EndPointPath.ACCOUNT_SAVE_ROLE_PATH)
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppRole addNewRole(@RequestBody AppRole appRole) throws Exception {
        return accountMetier.addNewRole(appRole);
    }

    @PostMapping(path = EndPointPath.ACCOUNT_ADD_ROLE_TO_USER_PATH)
    @PostAuthorize("hasAuthority('ADMIN')")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) throws Exception {
        accountMetier.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }

    @GetMapping(path = EndPointPath.ACCOUNT_FIND_BY_USERNAME_PATH)
    @PostAuthorize("hasAuthority('USER')")
    public AppUser findUserByUsername(@PathVariable("username") String username) throws Exception {
        return accountMetier.findUserByUsername(username);
    }

    @GetMapping(path = EndPointPath.ACCOUNT_FIND_BY_ROLE_NAME_PATH)
    @PostAuthorize("hasAuthority('USER')")
    public AppRole findRoleByRoleName(@PathVariable("name") String name) throws Exception {
        return accountMetier.findRoleByRoleName(name);
    }

    @GetMapping(path = EndPointPath.ACCOUNT_ALL_USERS_LIST_PATH)
    @PostAuthorize("hasAuthority('USER')")
    public List<AppUser> listUsers() {
        return accountMetier.listUsers();
    }

    @GetMapping(path = EndPointPath.ACCOUNT_ALL_ROLE_LIST_PATH)
    @PostAuthorize("hasAuthority('USER')")
    public List<AppRole> listRoles() {
        return accountMetier.listRoles();
    }

    @GetMapping(path = EndPointPath.ACCOUNT_REFRESH_TOKEN_PATH)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        accountMetier.refreshToken(request, response);
    }

    @GetMapping(path = EndPointPath.ACCOUNT_PROFILE_PATH)
    @PostAuthorize("hasAuthority('USER')")
    public AppUser profile(Principal principal) {
        return accountMetier.profile(principal);
    }
}
