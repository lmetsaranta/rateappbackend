package fi.academy.rateappbackend.repositories;

import fi.academy.rateappbackend.entities.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.content.id = :contentId")
    Like findByUserIdAndContentId(@Param("userId") Long userId, @Param("contentId") Long contentId);

    @Query("SELECT l.content.id FROM Like l WHERE l.user.id = :userId")
    Page<Long> findLikedContentIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.content.id = :contentIds")
    List<Like> findByUserIdAndContentIdIn(@Param("userId") Long userId, @Param("contentIds") List<Long> contentIds);
}
