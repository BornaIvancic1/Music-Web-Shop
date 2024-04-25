package hr.algebra.java_web.repository;

import hr.algebra.java_web.model.Album;
import hr.algebra.java_web.model.AlbumGenre;
import hr.algebra.java_web.model.JWUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AlbumGenreRepository extends JpaRepository<AlbumGenre, Long> {
    @Query(value = "EXEC DeleteByAlbumId:albumId", nativeQuery = true)
    void deleteByAlbumId(Long albumId);

    @Query(value = "EXEC FindByAlbumId:albumId", nativeQuery = true)
    AlbumGenre findByAlbumId(Long albumId);
}
