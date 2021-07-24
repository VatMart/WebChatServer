package ru.vatmart.webchatserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vatmart.webchatserver.entities.Room;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.entities.enums.Role;
import ru.vatmart.webchatserver.exceptions.UserExistException;
import ru.vatmart.webchatserver.payloads.requests.SignupRequest;
import ru.vatmart.webchatserver.repositories.RoomRepository;
import ru.vatmart.webchatserver.repositories.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    public static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoomRepository roomRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
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

    @Transactional
    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public Set<Room> getUserRooms(Principal principal) {
        User user = getUserByPrincipal(principal);
        return user.getRooms();
    }

    private User getUserByPrincipal(Principal principal) {
        String login = principal.getName();
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with login " + login));
    }

    public User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname).
                orElseThrow(() -> new UsernameNotFoundException("Username not found with nickname " + nickname));
    }
}
