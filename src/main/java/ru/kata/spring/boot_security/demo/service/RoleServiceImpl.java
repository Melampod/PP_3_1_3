package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private ru.kata.spring.boot_security.demo.dao.RoleDao roleDao;

    public RoleServiceImpl(ru.kata.spring.boot_security.demo.dao.RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public List<Role> findAllRoles() {
        return roleDao.findAllRoles();
    }

    @Override
    public void addRole(Role role) {
        roleDao.addRole(role);
    }

    @Override
    public Role findRoleById(Long id) {
        return roleDao.findRoleById(id);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleDao.findRoleByName(name);
    }

}