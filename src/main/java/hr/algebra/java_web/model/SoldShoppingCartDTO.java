package hr.algebra.java_web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SoldShoppingCartDTO {
    private SoldShoppingCart soldShoppingCart;
    private JWUser user;
    private String albumName;
    private List<ShoppingCartDetails> shoppingCartDetails;

}

