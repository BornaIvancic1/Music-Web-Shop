package hr.algebra.java_web.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItem {
    private Album album;
    private int numberOfOrders;

    public CartItem(Album album, int numberOfOrders) {
        this.album = album;
        this.numberOfOrders = numberOfOrders;
    }

}
