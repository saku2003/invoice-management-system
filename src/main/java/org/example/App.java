package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.util.JpaUtil;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();
        UserRepository userRepository = new UserRepository(emf);
        UserService userService = new UserService(userRepository);

        User user = new User();
        userRepository.save(user);
    }
}
