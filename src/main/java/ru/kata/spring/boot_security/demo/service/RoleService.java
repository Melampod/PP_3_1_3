package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;
import java.util.List;

public interface RoleService {

    List<Role> findAllRoles();
    void addRole(Role role);
    Role findRoleById(Long id);
    Role findRoleByName(String name);

}