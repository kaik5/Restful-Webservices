package com.appdeveloperblog.app.ws;

import java.util.Arrays;
import java.util.Collection;

import com.appdeveloperblog.app.ws.io.entity.AuthorityEntity;
import com.appdeveloperblog.app.ws.io.entity.RoleEntity;
import com.appdeveloperblog.app.ws.io.entity.UserEntity;
import com.appdeveloperblog.app.ws.io.respositories.AuthorityRepository;
import com.appdeveloperblog.app.ws.io.respositories.RoleRepository;
import com.appdeveloperblog.app.ws.io.respositories.UserRepository;
import com.appdeveloperblog.app.ws.shared.Roles;
import com.appdeveloperblog.app.ws.shared.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialUsersSetup {
    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener
    @Transactional
    public void onApllicationEvent(ApplicationReadyEvent event)
    {
        AuthorityEntity read =  createAuthorirty("AUTHORITY_READ");
        AuthorityEntity delete = createAuthorirty("AUTHORITY_DELETE");
        AuthorityEntity write = createAuthorirty("AUTHORITY_WRITE");

        createRole(Roles.ROLE_USER.name(), Arrays.asList(read, write));

        RoleEntity admin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(read, write, delete));

        if(admin == null) return;

        // hardcode the super user 
        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("test1");
        adminUser.setLastName("test1");
        adminUser.setEmail("test1@admin.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setEmailVerificationToken(null);
        adminUser.setUserId(utils.generateUserId(30));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123"));
        adminUser.setRoles(Arrays.asList(admin));
        userRepository.save(adminUser);
    }

    @Transactional
    private AuthorityEntity createAuthorirty(String name)
    {
        AuthorityEntity authority = authorityRepository.findByName(name);
        if(authority == null)
        {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }

        return authority;
    }

    @Transactional
    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities)
    {
        RoleEntity role = roleRepository.findByName(name);
        if(role == null)
        {
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
         

        }

        return role;
    }
}
