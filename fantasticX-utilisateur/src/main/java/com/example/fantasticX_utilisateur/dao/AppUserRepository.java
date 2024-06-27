package com.example.fantasticX_utilisateur.dao;

import com.example.fantasticX_utilisateur.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

AppUser findByUsername (String username);

}
