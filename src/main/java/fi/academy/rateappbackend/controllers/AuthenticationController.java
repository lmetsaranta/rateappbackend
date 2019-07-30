package fi.academy.rateappbackend.controllers;

import fi.academy.rateappbackend.entities.Role;
import fi.academy.rateappbackend.entities.User;
import fi.academy.rateappbackend.repositories.RoleRepository;
import fi.academy.rateappbackend.repositories.UserRepository;
import fi.academy.rateappbackend.security.JwtTokenProvider;
import fi.academy.rateappbackend.utils.ApiRequest;
import fi.academy.rateappbackend.utils.JwtAuthenticationRequest;
import fi.academy.rateappbackend.utils.SignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashSet;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity(new ApiRequest(false, "Käyttäjänimi on jo varattu."),
                    HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity(new ApiRequest(false, "Sähköpostiosoite on jo käytössä."),
                    HttpStatus.BAD_REQUEST);
        }
        User newUser = new User(user.getName(), user.getUsername(), user.getEmail(), user.getPassword());

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        HashSet<Role> role = new HashSet<>();
        role.add(new Role("ROLE_USER"));

        newUser.setRoles(role);

        userRepository.save(newUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{kayttajanimi}")
                .buildAndExpand(newUser.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiRequest(true, "Käyttäjä luotu onnistuneesti."));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsernameOrEmail(),
                        signInRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationRequest(jwt));
    }

}
