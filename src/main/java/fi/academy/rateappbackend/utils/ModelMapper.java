package fi.academy.rateappbackend.utils;

import fi.academy.rateappbackend.entities.Content;
import fi.academy.rateappbackend.entities.User;

public class ModelMapper {

    public static ContentResponse mapContentToContentResponse(Content content, User user, Long userLike) {
        ContentResponse contentResponse = new ContentResponse();
        contentResponse.setId(content.getId());
        contentResponse.setHeadline(content.getHeadline());
        contentResponse.setText(content.getText());
        contentResponse.setCreatedAt(content.getCreatedAt());

        UserDetails userDetails = new UserDetails(user.getId(), user.getUsername(), user.getName());
        contentResponse.setCreatedBy(userDetails);

        if(userLike != null) {
            contentResponse.setIsLiked(userLike);
        }

        return contentResponse;
    }
}
