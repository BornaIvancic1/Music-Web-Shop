package hr.algebra.java_web.repository;

import hr.algebra.java_web.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {



    @Query(value = "EXEC FindAlbumsByGenreId:genreId", nativeQuery = true)
    List<Album> findByGenreId(Long genreId);


    @Query(value = "EXEC FindByArtistContaining:artist", nativeQuery = true)
    List<Album> findByArtistContaining(String artist);

    @Query(value = "EXEC FindByTitleContaining:albumName", nativeQuery = true)
    List<Album> findByTitleContaining(String albumName);
}
