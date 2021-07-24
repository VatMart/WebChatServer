package ru.vatmart.webchatserver.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.entities.enums.Role;
import ru.vatmart.webchatserver.exceptions.UserExistException;
import ru.vatmart.webchatserver.payloads.requests.SigninRequest;
import ru.vatmart.webchatserver.payloads.requests.SignupRequest;
import ru.vatmart.webchatserver.payloads.responses.JwtResponse;
import ru.vatmart.webchatserver.payloads.responses.MessageResponse;
import ru.vatmart.webchatserver.security.JWTTokenProvider;
import ru.vatmart.webchatserver.services.UserService;
import ru.vatmart.webchatserver.validation.ResponseErrorValidation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    private JWTTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private ResponseErrorValidation responseErrorValidation;
    private UserService userService;

    @Autowired
    public AuthController(JWTTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, ResponseErrorValidation responseErrorValidation, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.responseErrorValidation = responseErrorValidation;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {
        // Checking errors
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        // Create new user's account
        try {
            userService.createUser(signUpRequest);
        } catch(UserExistException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody SigninRequest signinRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signinRequest.getLogin(),
                signinRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtAccToken = jwtTokenProvider.generateToken(authentication, JWTTokenProvider.JwtTokenType.access);

        //return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));

        User userDetails = (User) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(userDetails.getId(),
                jwtAccToken,
                jwtTokenProvider.getExpiredTime(jwtAccToken),
                roles));
    }
}
