package fi.academy.rateappbackend.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @OneToOne(mappedBy = "image")
    public Content content;

    public Image() {
    }

    public Image(@NotBlank String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

