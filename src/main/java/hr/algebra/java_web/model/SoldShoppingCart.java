package hr.algebra.java_web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "SoldShoppingCart")
public class SoldShoppingCart implements Serializable {
    @Setter
    @Getter
    @jakarta.persistence.Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long jWUser_Id;
    private Long album_Id;
    private Long number_of_orders;




    public SoldShoppingCart() {
    }


    public Long getAlbumId() {
        return album_Id;
    }

    public Long getjWUser_Id() {
        return jWUser_Id;
    }

    public void setAlbumId(Long album_Id) {
        this.album_Id = album_Id;
    }

    public Long getNumberOfOrders() {
        return number_of_orders;
    }

    public void setNumberOfOrders(Long number_of_orders) {
        this.number_of_orders = number_of_orders;
    }
}

