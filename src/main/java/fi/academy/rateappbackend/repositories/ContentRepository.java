package fi.academy.rateappbackend.repositories;

import fi.academy.rateappbackend.entities.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Optional<Content> findById(Long id);

    Page<Content> findByCreatedBy(Long userId, Pageable pageable);

}
