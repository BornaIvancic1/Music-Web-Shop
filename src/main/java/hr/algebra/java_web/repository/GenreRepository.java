package hr.algebra.java_web.repository;


import hr.algebra.java_web.model.Album;
import hr.algebra.java_web.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query(value = "EXEC findGenresByAlbums", nativeQuery = true)
    List<Genre> findGenresByAlbums();

}
