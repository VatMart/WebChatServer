package ru.vatmart.webchatserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.exceptions.UserExistException;
import ru.vatmart.webchatserver.repositories.UserRepository;

import javax.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    //TODO delete this
    public void printUsers() {
        System.out.println(userRepository.findAll());
    }

}
