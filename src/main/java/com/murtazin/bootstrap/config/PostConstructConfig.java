package com.murtazin.bootstrap.config;

import com.murtazin.bootstrap.model.Role;
import com.murtazin.bootstrap.model.User;
import com.murtazin.bootstrap.repository.RoleRepository;
import com.murtazin.bootstrap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class PostConstructConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public PostConstructConfig(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void addRolesAndUsersInDB() {
        checkRole("ROLE_ADMIN");
        checkRole("ROLE_USER");
        checkAdmin();
        checkUser();
    }

    private void checkRole(String roleName) {
        if(!roleRepository.existsByName(roleName)){
            roleRepository.save(new Role(roleName));
        }
    }

    private void checkAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepository.findByName("ROLE_ADMIN"));
            roleSet.add(roleRepository.findByName("ROLE_USER"));
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder().encode("admin"));
            user.setLastname("adminov");
            user.setEmail("admin@mail.ru");
            user.setAge(33);
            user.setRoles(roleSet);
            userRepository.save(user);
        }
    }

    private void checkUser() {
        if(!userRepository.existsByUsername("user")){
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepository.findByName("ROLE_USER"));
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder().encode("user"));
            user.setLastname("userov");
            user.setEmail("user@mail.ru");
            user.setAge(44);
            user.setRoles(roleSet);
            userRepository.save(user);
        }
    }
}
