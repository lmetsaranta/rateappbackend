package fi.academy.rateappbackend.controllers;

import fi.academy.rateappbackend.entities.Content;
import fi.academy.rateappbackend.exceptions.ResourceNotFoundException;
import fi.academy.rateappbackend.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ContentController {

    @Autowired
    ContentRepository contentRepository;

    @GetMapping("/content")
    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }

    @GetMapping("/content/{id}")
    public Content getContentById(@PathVariable(value = "id") Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sisältöä", "Id", id));
        return content;
    }

    @PostMapping("/content")
    public ResponseEntity<?> addContent(@RequestBody Content content) {
        Content added = contentRepository.save(content);
        URI location = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/content/{id}")
                .buildAndExpand(added.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
