package fi.academy.rateappbackend.service;

import fi.academy.rateappbackend.entities.Content;
import fi.academy.rateappbackend.entities.Like;
import fi.academy.rateappbackend.entities.User;
import fi.academy.rateappbackend.exceptions.BadRequestException;
import fi.academy.rateappbackend.exceptions.ResourceNotFoundException;
import fi.academy.rateappbackend.repositories.ContentRepository;
import fi.academy.rateappbackend.repositories.LikeRepository;
import fi.academy.rateappbackend.repositories.UserRepository;
import fi.academy.rateappbackend.security.UserPrincipal;
import fi.academy.rateappbackend.utils.ContentResponse;
import fi.academy.rateappbackend.utils.ModelMapper;
import fi.academy.rateappbackend.utils.PageableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ContentService {

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UserRepository userRepository;

    public PageableResponse<ContentResponse> getAllContent(UserPrincipal currentUser, int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Content> contents = contentRepository.findAll(pageable);

        if (contents.getNumberOfElements() == 0) {
            return new PageableResponse<>(Collections.emptyList(), contents.getNumber(),
                    contents.getSize(), contents.getTotalElements(), contents.getTotalPages(), contents.isLast());
        }

        List<Long> contentIds = contents.map(Content::getId).getContent();
        Map<Long, Long> contentUserLikeMap = getContentUserLikeMap(currentUser, contentIds);
        Map<Long, User> creatorMap = getContentCreatorMap(contents.getContent());

        List<ContentResponse> contentResponses = contents.map(content -> {
            return ModelMapper.mapContentToContentResponse(content,
                    creatorMap.get(content.getCreatedBy()),
                    contentUserLikeMap == null ? null : contentUserLikeMap.getOrDefault(content.getId(), null));
        }).getContent();

        return new PageableResponse<>(contentResponses, contents.getNumber(), contents.getSize(), contents.getTotalElements(), contents.getTotalPages(), contents.isLast());
    }

    public PageableResponse<ContentResponse> getContentCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Käyttäjä", "käyttäjänimi", username));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Content> contents = contentRepository.findByCreatedBy(user.getId(), pageable);

        if (contents.getNumberOfElements() == 0) {
            return new PageableResponse<>(Collections.emptyList(), contents.getNumber(),
                    contents.getSize(), contents.getTotalElements(), contents.getTotalPages(), contents.isLast());
        }

        List<Long> contentIds = contents.map(Content::getId).getContent();
        Map<Long, Long> contentUserLikeMap = getContentUserLikeMap(currentUser, contentIds);
//        Map<Long, User> creatorMap = getContentCreatorMap(contents.getContent());

        List<ContentResponse> contentResponses = contents.map(content -> {
            return ModelMapper.mapContentToContentResponse(content,
                    user,
//                    creatorMap.get(content.getCreatedBy()),
                    contentUserLikeMap == null ? null : contentUserLikeMap.getOrDefault(content.getId(), null));
        }).getContent();

        return new PageableResponse<>(contentResponses, contents.getNumber(), contents.getSize(), contents.getTotalElements(), contents.getTotalPages(), contents.isLast());
    }

    private Map<Long, Long> getContentUserLikeMap(UserPrincipal currentUser, List<Long> contentIds) {
        Map<Long, Long> contentUserLikeMap = null;
        if (currentUser != null) {
            List<Like> userLikes = likeRepository.findByUserIdAndContentIdIn(currentUser.getId(), contentIds);

            contentUserLikeMap = userLikes.stream()
                    .collect(Collectors.toMap(l -> l.getUser().getId(), l -> l.getContent().getId()));
        }
        return contentUserLikeMap;
    }

    private Map<Long, User> getContentCreatorMap(List<Content> contents) {
        List<Long> creatorIds = contents.stream()
                .map(Content::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}
