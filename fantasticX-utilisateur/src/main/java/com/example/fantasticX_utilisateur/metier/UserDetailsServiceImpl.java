package com.example.fantasticX_utilisateur.metier;

import com.example.fantasticX_utilisateur.entity.AppUser;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountMetier accountMetier;

    public UserDetailsServiceImpl(AccountMetier accountMetier) {
        this.accountMetier = accountMetier;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountMetier.findUserByUsername(username);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        appUser.getAppRoles().forEach(r->{
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(r.getRoleName());
            authorities.add(grantedAuthority);
        });
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
