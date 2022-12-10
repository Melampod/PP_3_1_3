package ru.kata.spring.boot_security.demo.dataInit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInit {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DataInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        List<User> users = userService.findAllUsers();
        if (users == null) {
            if (roleService.findAllRoles().isEmpty()) {
                roleService.addRole(new Role("ROLE_ADMIN"));
                roleService.addRole(new Role("ROLE_USER"));
            }
            Role admin = roleService.findRoleById(1L);
            Role user = roleService.findRoleById(2L);
            Set<Role> adminRole = new HashSet<>();
            Set<Role> userRole = new HashSet<>();
            Set<Role> userAndAdminRole = new HashSet<>();
            adminRole.add(admin);
            userRole.add(user);
            userAndAdminRole.add(user);
            userAndAdminRole.add(admin);
            userService.saveUser(new User("admin", ("admin"), 12, "admin@mail.ru", adminRole));
            userService.saveUser(new User("user", ("user"), 34, "user@mail.ru", userRole));
            userService.saveUser(new User("useradmin", ("useradmin"), 56, "useradmin@mail.ru", userAndAdminRole));
        }

    }
}