package com.example.fantasticX_utilisateur.dao;

import com.example.fantasticX_utilisateur.entity.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

    AppRole findByRoleName (String roleName);

}
