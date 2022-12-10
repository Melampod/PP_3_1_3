package ru.kata.spring.boot_security.demo.dao;


import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllUsers() {
        List<User> usersList = entityManager.createQuery("from User", User.class)
                .getResultList();
        if (usersList.isEmpty()) return null;
        return usersList;
    }

    @Override
    public void saveUser(User user) {
        User newUser = entityManager.merge(user);
        user.setId(newUser.getId());
    }

    @Override
    public User findUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        User user = entityManager.createQuery("select u from User u join fetch u.roles where u.username =:username", User.class)
                .setParameter("username", username)
                .getResultList()
                .stream().findFirst().orElse(null);
        return user;
    }


    @Override
    public boolean deleteUser(Long id) {
        User user = entityManager.find(User.class, id);

        Set<Role> roles = user.getRoles();
        List<User> usersList = new ArrayList<>();
        for (Role role : roles) {
            if (role.getName().equals("ROLE_ADMIN")) {
                usersList = entityManager.
                        createQuery("select u from User u join fetch u.roles as r where r.id =:roleAdminId", User.class)
                        .setParameter("roleAdminId", 1L)
                        .getResultList();
                if (usersList.size() == 1) {
                    return false;
                }
            }
        }
        entityManager.remove(user);
        return true;
    }
}