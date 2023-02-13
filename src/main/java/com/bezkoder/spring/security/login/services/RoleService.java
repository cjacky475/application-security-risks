package com.bezkoder.spring.security.login.services;

import com.bezkoder.spring.security.login.models.ERole;
import com.bezkoder.spring.security.login.models.Role;
import com.bezkoder.spring.security.login.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        roleRepository.save(new Role(ERole.ROLE_ADMIN));
        roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        roleRepository.save(new Role(ERole.ROLE_USER));
    }

}