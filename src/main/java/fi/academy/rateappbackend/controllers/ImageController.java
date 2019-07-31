package fi.academy.rateappbackend.controllers;


import fi.academy.rateappbackend.entities.Image;
import fi.academy.rateappbackend.exceptions.ResourceNotFoundException;
import fi.academy.rateappbackend.repositories.ImageRepository;
import fi.academy.rateappbackend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@CrossOrigin
@RequestMapping("/api/img")
public class ImageController {

@Autowired
    ImageRepository imageRepository;

@Autowired
    ImageService imageService;

@GetMapping(value = "/{filename}", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource findImageByFilename(@PathVariable(name = "filename") String filename) {
    Resource resource = null;
    try {
        resource = imageService.findOneImage(filename);
    } catch (ResourceNotFoundException r) {
        throw  r;
    }
    return resource;
}
}
