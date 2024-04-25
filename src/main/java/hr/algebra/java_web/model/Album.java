package hr.algebra.java_web.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "Album")
public class Album {
    @jakarta.persistence.Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
private String image_url;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    private int sells;
    public Album() {
    }

    public Album(String title, String artist, String image_url, Date releaseDate,  int sells) {
        this.title = title;
        this.artist = artist;
        this.image_url = image_url;
        this.releaseDate = releaseDate;

        this.sells = sells;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String imageUrl) {
        this.image_url = imageUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }





    public int getSells() {
        return sells;
    }

    public void setSells(int sells) {
        this.sells = sells;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
