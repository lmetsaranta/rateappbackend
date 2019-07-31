package fi.academy.rateappbackend.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

public class ContentResponse {
private Long id;
private String headline;
private String text;
private UserDetails createdBy;
private Instant createdAt;

@JsonInclude(JsonInclude.Include.NON_NULL)
private Long isLiked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDetails getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDetails createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Long isLiked) {
        this.isLiked = isLiked;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
