package ru.kata.spring.boot_security.demo.dao;


import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllUsers() {
        List<User> usersList = entityManager.createQuery("select distinct u from User u join fetch u.roles", User.class)
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
        // User user = entityManager.find(User.class, id);
        User user = entityManager.createQuery("select u from User u join fetch u.roles where u.id =:id", User.class)
                .setParameter("id", id)
                .getResultList()
                .stream().findFirst().orElse(null);
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
        if (user.isAdmin()
                && numOfAdminRole() == 1) {
            return false;
        }
        entityManager.remove(user);
        return true;
    }

    @Override
    public int numOfAdminRole() {
        List<User> usersList = entityManager
                .createQuery("select u from User u join fetch u.roles as r where r.id =:roleAdminId", User.class)
                .setParameter("roleAdminId", 1L)
                .getResultList();
        return usersList.size();
    }
}