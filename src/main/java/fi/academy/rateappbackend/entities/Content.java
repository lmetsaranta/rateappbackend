package fi.academy.rateappbackend.entities;

import fi.academy.rateappbackend.audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Content extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    @Size(max = 40)
    private String headline;

    @NotBlank
    @Size(max = 255)
    private String text;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "image_fk")
    public Image image;


    public Content() {
    }

    public Content(@NotBlank @Size(max = 40) String headline, @NotBlank @Size(max = 255) String text) {
        this.headline = headline;
        this.text = text;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
