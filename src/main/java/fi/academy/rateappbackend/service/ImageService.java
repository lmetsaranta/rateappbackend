package fi.academy.rateappbackend.service;

import fi.academy.rateappbackend.entities.Image;
import fi.academy.rateappbackend.exceptions.ResourceNotFoundException;
import fi.academy.rateappbackend.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {

    private static String UPLOAD_ROOT = "C:\\Users\\sanna\\rateappfrontend\\public";

    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ImageService(ImageRepository imageRepository, ResourceLoader resourceLoader) {

        this.imageRepository = imageRepository;
        this.resourceLoader = resourceLoader;
    }

    public Resource findOneImage(String filename) throws ResourceNotFoundException {
        return resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename);
    }

    public Image createImage(MultipartFile file) throws IOException {
        Image created = null;
        if(!file.isEmpty()) {
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, file.getOriginalFilename()));
            created = imageRepository.save(new Image(file.getOriginalFilename()));
        }
        return created;
    }

    public void DeleteImage(String filename) throws IOException {
        final Image byName = imageRepository.findByName(filename);
        imageRepository.delete(byName);
        Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
    }
}
