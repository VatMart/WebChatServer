package ru.vatmart.webchatserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vatmart.webchatserver.dto.UserDTO;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.facade.UserFacade;
import ru.vatmart.webchatserver.services.UserService;
import ru.vatmart.webchatserver.validation.ResponseErrorValidation;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
//@CrossOrigin
public class UserController {

    private UserService userService;
    private UserFacade userFacade;
    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService, UserFacade userFacade, ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    //TODO UPDATE USER

}
