package hr.algebra.java_web.controller;
import hr.algebra.java_web.model.Album;
import hr.algebra.java_web.repository.AlbumRepository;
import hr.algebra.java_web.utility.LoginUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BestSellersController {

    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping("/bestSellers")
    public ResponseEntity<List<Album>> getBestSellers(
            @RequestParam(name = "artist", required = false) String artist,
            @RequestParam(name = "albumName", required = false) String albumName,
            HttpServletRequest request) {

        if (!LoginUtility.isAdmin(request)) {
            List<Album> albums;

            if (artist != null && !artist.isEmpty()) {
                albums = albumRepository.findByArtistContaining(artist);
            } else if (albumName != null && !albumName.isEmpty()) {
                albums = albumRepository.findByTitleContaining(albumName);
            } else {
                albums = albumRepository.findAll();
            }

            albums.sort(Comparator.comparingInt(Album::getSells).reversed());

            return ResponseEntity.ok().body(albums);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
