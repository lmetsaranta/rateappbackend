package fi.academy.rateappbackend.controllers;

import fi.academy.rateappbackend.entities.User;
import fi.academy.rateappbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

@PostMapping("/signup")
public ResponseEntity<?> createUser(@RequestBody User user) {
User created = userRepository.save(user);
    URI location = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .path("/users/{id}")
            .buildAndExpand(created.getId())
            .toUri();
    return ResponseEntity.created(location).build();
}

//@PostMapping("/signin")
//public ResponseEntity<?> authenticateUser(SigninRequest signinRequest) {
//// tähän tulee autentikointi jossain vaiheessa.
//}

}
