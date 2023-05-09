package com.murtazin.bootstrap.service;

import com.murtazin.bootstrap.model.Role;
import com.murtazin.bootstrap.model.User;
import com.murtazin.bootstrap.repository.RoleRepository;
import com.murtazin.bootstrap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUser(User user) {
        String pass = passwordEncoder.encode(user.getPassword());
        user.setPassword(pass);
        userRepo.save(user);
    }

    @Override
    public void update(User user) {

        if (user.getPassword()=="") {
            String password = userRepo.getById(user.getId()).getPassword();
            user.setPassword(password);
        } else {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
        }


        if (user.getRoles().isEmpty()){

            user.setRoles(userRepo.getById(user.getId()).getRoles());
        }

        userRepo.save(user);
    }

    @Override
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Override
    public Set<Role> usersRole(User user) {
        return null;
    }

    @Override
    public List<User> allUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByName(String name) {
        return userRepo.findByUsername(name);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepo.findById(userId).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username);
    }
}

