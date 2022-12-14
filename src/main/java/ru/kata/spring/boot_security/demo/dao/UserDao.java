package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.User;
import java.util.List;

public interface UserDao {

    List<User> findAllUsers();
    void saveUser(User user);
    User findUserById(Long id);
    User findUserByUsername(String username);
    boolean deleteUser(Long id);
    int numOfAdminRole();

}