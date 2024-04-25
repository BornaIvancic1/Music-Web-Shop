package hr.algebra.java_web.controller;

import hr.algebra.java_web.model.Album;
import hr.algebra.java_web.model.AlbumGenre;
import hr.algebra.java_web.model.Genre;
import hr.algebra.java_web.repository.AlbumGenreRepository;
import hr.algebra.java_web.repository.AlbumRepository;
import hr.algebra.java_web.repository.GenreRepository;
import hr.algebra.java_web.repository.ShoppingCartRepository;
import hr.algebra.java_web.utility.LoginUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/musicWebShop")
@AllArgsConstructor
public class MusicController {
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final AlbumGenreRepository albumGenreRepository;
  private final  ShoppingCartRepository shoppingCartRepository;
    @GetMapping("/categories.html")
    public String getAllMusicCategories(Model model, HttpServletRequest request) {
        if (LoginUtility.isAdmin(request)){
            return "Error";
        }else{
        List<Genre> genres = genreRepository.findGenresByAlbums();
        model.addAttribute("genres", genres);
        return "Categories";
    }}

    @GetMapping("/newReleases.html")
    public String getNewReleases(Model model,
                                 @RequestParam(name = "artist", required = false) String artist,
                                 @RequestParam(name = "albumName", required = false) String albumName, HttpServletRequest request) {
        if (LoginUtility.isAdmin(request)){
            return "Error";
        }else{
        List<Album> albums;

        if (artist != null && !artist.isEmpty()) {
            albums = albumRepository.findByArtistContaining(artist);
        } else if (albumName != null && !albumName.isEmpty()) {

            albums = albumRepository.findByTitleContaining(albumName);
        } else {

            albums = albumRepository.findAll();
        }


        albums.sort(Comparator.comparing(Album::getReleaseDate).reversed());

        model.addAttribute("albums", albums);
        return "NewReleases";
    }}

    @GetMapping("/bestSellers.html")
    public String getBestSellers(Model model,
                                HttpServletRequest request) {
        if (!LoginUtility.isAdmin(request)) {

            return "BestSellers";

        } else {
            return "Error";
        }
    }
    @GetMapping("/albumCRUD.html")
    public String getAlbumCRUD(Model model, HttpServletRequest request) {
        if (!LoginUtility.isAdmin(request)){
            return "Error";
        }else{
        List<Album> albums = albumRepository.findAll();
        albums.sort(Comparator.comparingInt(Album::getSells).reversed());
        List<Genre> genres = genreRepository.findAll();
        List<AlbumGenre> albumGenres = albumGenreRepository.findAll();

        model.addAttribute("albums", albums);
        model.addAttribute("genres", genres);
        model.addAttribute("albumGenres", albumGenres);
        return "AlbumCRUD";
    }}


    @GetMapping("/genreCRUD.html")
    public String getGenreCRUD(Model model,HttpServletRequest request) {
        if (!LoginUtility.isAdmin(request)){
            return "Error";
        }else{
        List<Genre> genres = genreRepository.findAll();
        model.addAttribute("genres", genres);
        return "GenreCRUD";
    }}

    @PostMapping("/genre/create")
    public String createGenre(@ModelAttribute("genre") Genre genre) {
        genreRepository.save(genre);
        return "redirect:/musicWebShop/genreCRUD.html";
    }
    @PostMapping("/genre/{id}/update")
    public String updateGenre(@PathVariable Long id, @ModelAttribute Genre genreDetails) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre Id:" + id));
        genre.setName(genreDetails.getName());

        genreRepository.save(genre);
        return "redirect:/musicWebShop/genreCRUD.html";
    }

    @PostMapping("/genre/{id}/delete")
    public String deleteGenre(@PathVariable Long id) {
        genreRepository.deleteById(id);
        return "redirect:/musicWebShop/genreCRUD.html";
    }

    @PostMapping("/album/create")
    public String createAlbum(@ModelAttribute("album") Album album, @RequestParam("genreId") Long genreId) {
        Genre selectedGenre = genreRepository.findById(genreId).orElse(null);


            Album savedAlbum = albumRepository.save(album);

            AlbumGenre albumGenre = new AlbumGenre();
            albumGenre.setAlbumId(savedAlbum.getId());
        assert selectedGenre != null;
        albumGenre.setGenreId(selectedGenre.getId());


            albumGenreRepository.save(albumGenre);

        return "redirect:/musicWebShop/albumCRUD.html";
    }




    @PostMapping("/album/{id}/update")
    public String updateAlbum(@PathVariable Long id, @ModelAttribute Album albumDetails, @RequestParam("genreId") Long genreId) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + id));

        album.setTitle(albumDetails.getTitle());
        album.setArtist(albumDetails.getArtist());
        album.setImageUrl(albumDetails.getImageUrl());

        albumRepository.save(album);

        AlbumGenre albumGenre = albumGenreRepository.findByAlbumId(album.getId());

            if (albumGenre.getGenreId().equals(genreId)) {
                return "redirect:/musicWebShop/albumCRUD.html";
            } else {

                albumGenre.setGenreId(genreId);
                albumGenreRepository.save(albumGenre);

            }


        return "redirect:/musicWebShop/albumCRUD.html";
    }


    @PostMapping("/album/{id}/delete")
    public String deleteAlbum(@PathVariable Long id) {
        albumGenreRepository.deleteByAlbumId(id);

        shoppingCartRepository.removeAlbumFromAllCarts(id);
        albumRepository.deleteById(id);

        return "redirect:/musicWebShop/albumCRUD.html";
    }


    @PostMapping("/addToCart")
    public String addToCart(long albumId, Integer quantity, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");

        if (userId == null) {

            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }
            cart.put(albumId, quantity);
            session.setAttribute("cart", cart);
        } else {
            shoppingCartRepository.addToCart(userId, albumId, quantity);
        }

        return "redirect:/musicWebShop/shoppingCart.html";
    }





    @PostMapping("/removeFromCart")
    public String removeFromCart(long albumId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");

        if (userId == null) {
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart != null && cart.containsKey(albumId)) {
                cart.remove(albumId); // Remove the item from cart
                session.setAttribute("cart", cart);
            }
            return "redirect:/musicWebShop/shoppingCart.html";
        } else {
            shoppingCartRepository.removeFromCart(userId, albumId);
            return "redirect:/musicWebShop/shoppingCart.html";
        }
    }


    @PostMapping("/updateCartItem")
    public String updateCartItem(long albumId, Integer quantity, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");

        if (userId == null) {
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart != null) {
                if (quantity != null) {
                    cart.put(albumId, quantity);
                    session.setAttribute("cart", cart);
                }
            }
        } else {

            if (quantity != null) {
                shoppingCartRepository.updateCartItem(userId, albumId, quantity);
            }
        }

        return "redirect:/musicWebShop/shoppingCart.html";
    }


    @GetMapping("/albumByGenre")
    public String getAlbumByGenre(@RequestParam("genreId") Long genreId, Model model, HttpServletRequest request) {
        if (!LoginUtility.isAdmin(request)){
        List<Album> albums = albumRepository.findByGenreId(genreId);
        albums.sort(Comparator.comparingInt(Album::getSells).reversed());
        model.addAttribute("albums", albums);
        return "AlbumByGenre";}else{
            return "Error";
        }
    }


    @ModelAttribute("httpServletRequest")
    public HttpServletRequest getCurrentRequest(HttpServletRequest request) {
        return request;
    }
}
