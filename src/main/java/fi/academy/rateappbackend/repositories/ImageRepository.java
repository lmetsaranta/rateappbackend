package fi.academy.rateappbackend.repositories;

import fi.academy.rateappbackend.entities.Image;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {

    public Image findByName(String name);
}
