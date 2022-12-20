package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        List<User> usersList = userService.findAllUsers();
        model.addAttribute("usersList", usersList);
        model.addAttribute("numOfAdmin", userService.numOfAdminRole()); // чтобы не выводить кнопку Delete во view
        return "user-list";
    }

    @GetMapping("/addUser")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAllRoles());
        return "user-info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           @RequestParam(value = "rolees", required = false) Set<String> rolesNames,
                           Principal principal,
                           Model model) {

        Set<Role> roles = new HashSet<>();
        User userFromBD = userService.loadUserByUsername(user.getUsername());
        if (bindingResult.hasErrors()
                || (userFromBD != null && (userFromBD.getId() != user.getId()))) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.findAllRoles());
            if (userFromBD != null && (userFromBD.getId() != user.getId())) {
                model.addAttribute("notUnique", "Username is not unique!  ");
            }
            return "user-info";
        }

        for (String roleName : rolesNames) {
            roles.add(roleService.findRoleByName(roleName));}
        user.setRoles(roles);
        userService.saveUser(user);

        User userFromWeb = userService.loadUserByUsername(principal.getName());
        if (userFromWeb == null || !userFromWeb.isAdmin()) { // перезайти, если текущий пользователь изменил свой же username, или снял с себя ROLE_ADMIN
            return "redirect:/login";
        }
        return "redirect:/admin";
    }


    @GetMapping("/updateUser/{id}")
    public String updateUser(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAllRoles());
        model.addAttribute("numOfAdmin", userService.numOfAdminRole()); // чтобы при изменении admin у него НЕ была забрана ROLE_ADMIN,
        // если он последний admin в БД

        return "user-info";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, Principal principal) {
        userService.deleteUser(id);
        User userFromWeb = userService.loadUserByUsername(principal.getName());
        if (userFromWeb == null) { // если удалил самого себя из бд, то выйти
            return "redirect:/login";
        }
        return "redirect:/admin";
    }
}