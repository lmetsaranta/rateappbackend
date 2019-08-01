package fi.academy.rateappbackend.controllers;

import fi.academy.rateappbackend.entities.Content;
import fi.academy.rateappbackend.entities.Image;
import fi.academy.rateappbackend.exceptions.ResourceNotFoundException;
import fi.academy.rateappbackend.repositories.ContentRepository;
import fi.academy.rateappbackend.security.CurrentUser;
import fi.academy.rateappbackend.security.UserPrincipal;
import fi.academy.rateappbackend.service.ContentService;
import fi.academy.rateappbackend.service.ImageService;
import fi.academy.rateappbackend.utils.ContentResponse;
import fi.academy.rateappbackend.utils.PageableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ContentController {

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    ContentService contentService;

    @Autowired
    ImageService imageService;


    /*
    * Method returns all content from database in pageable response and authenticates current user.
    * Content includes headline, text, image, information about user creation time and if the content
    * has been liked by current user.
    * @return pageable response of all contents.
    */
    @GetMapping("/content")
    public PageableResponse<ContentResponse> getAllContent(@CurrentUser UserPrincipal currentUser,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "size") int size) {
        return contentService.getAllContent(currentUser, page, size);
    }


    @GetMapping("/content/{id}")
    public Content getContentById(@PathVariable(value = "id") Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sisältöä", "Id", id));
        return content;
    }

    /*
     * Method authenticates current user and returns all content from database in pageable response
     * that are created by current user.
     * Content includes headline, text, image, information about user creation time and if the content
     * has been liked by current user.
     * @return pageable response of all contents.
     */
    @GetMapping("/content/me/{username}")
    public PageableResponse<ContentResponse> getContentCreatedByCurrentUser(@PathVariable(value = "username") String username,
                                                                            @CurrentUser UserPrincipal currentUser,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "size") int size) {
        return contentService.getContentCreatedBy(username, currentUser, page, size);
    }

    /*
    * */
    @PostMapping("/contentAndImage")
    public ResponseEntity<?> addContentAndImage(@RequestParam String headline,
                                                @RequestParam String text,
                                                @RequestParam MultipartFile multipartFile) throws IOException {
        Image image = imageService.createImage(multipartFile);

        Content content = new Content(headline, text);
        content.setImage(image);
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

    @PostMapping("/content/like")
    public ContentResponse castLike(@CurrentUser UserPrincipal currentUser,
                                      @RequestBody Long contentId) {
return contentService.castLikeAndRefreshContent(contentId, currentUser);
    }
}
