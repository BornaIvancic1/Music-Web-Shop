package hr.algebra.java_web.repository;


import hr.algebra.java_web.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query(value = "EXEC GetShoppingCartItemsByUserId :userId", nativeQuery = true)
    List<ShoppingCart> findByJWUserId(Long userId);

    @Query(value = "EXEC GetShoppingCartAmountByUserId :userId", nativeQuery = true)
    Double amount(Long userId);

    @Query(value = "EXEC GetShoppingCartAlbumNamesByUserId :userId", nativeQuery = true)
    String albumNames(Long userId);

    @Query(value = "EXEC TransferToShoppingCartSoldForUser :userId", nativeQuery = true)
    void transferToShoppingCartSold(Long userId);




    @Query(value = "EXEC AddToCart :userId, :albumId, :quantity", nativeQuery = true)
    void addToCart(Long userId, Long albumId, int quantity);

    @Query(value = "EXEC RemoveFromCart :userId, :albumId", nativeQuery = true)
    void removeFromCart(Long userId, Long albumId);

    @Query(value = "EXEC UpdateCartItem :userId, :albumId, :quantity", nativeQuery = true)
    void updateCartItem(Long userId, Long albumId, int quantity);

    @Query(value = "EXEC RemoveAlbumFromAllCarts:albumId", nativeQuery = true)
    void removeAlbumFromAllCarts(Long albumId);
}