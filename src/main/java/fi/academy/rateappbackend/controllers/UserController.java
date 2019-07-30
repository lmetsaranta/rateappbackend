package fi.academy.rateappbackend.controllers;

import fi.academy.rateappbackend.entities.User;
import fi.academy.rateappbackend.exceptions.ResourceNotFoundException;
import fi.academy.rateappbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/{username}")
    public User getUserByUsername(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Käyttäjää", "käyttäjänimi", username));

        return user;
    }

}
