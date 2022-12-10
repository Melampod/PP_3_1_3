package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAllUsers();
    void saveUser(User user);
    User findUserById(Long id);
    User findUserByUsername(String username);
    boolean deleteUser(Long id);
    User loadUserByUsername(String username);
}