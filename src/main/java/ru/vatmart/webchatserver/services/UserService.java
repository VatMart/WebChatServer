package ru.vatmart.webchatserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.entities.enums.Role;
import ru.vatmart.webchatserver.exceptions.UserExistException;
import ru.vatmart.webchatserver.payloads.requests.SignupRequest;
import ru.vatmart.webchatserver.repositories.UserRepository;

import javax.transaction.Transactional;

@Service
public class UserService {
    public static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(SignupRequest signupRequest) {
        User user = new User();
        user.setLogin(signupRequest.getLogin());
        user.setNickname(signupRequest.getNickname());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        try {
            logger.info("Saving User {}", signupRequest.getLogin());
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    //TODO delete this
    public void printUsers() {
        System.out.println(userRepository.findAll());
    }

}
