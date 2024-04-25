package hr.algebra.java_web.repository;


import hr.algebra.java_web.model.ShoppingCartDetails;
import hr.algebra.java_web.model.SoldShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ShoppingCartDetailsRepository extends JpaRepository<ShoppingCartDetails, Long> {

    @Query(value = "EXEC InsertDetails :soldShoppingCartID, :purchaseDateTime, :purchaseMethod", nativeQuery = true)
    void insertDetails(long soldShoppingCartID, LocalDateTime purchaseDateTime, String purchaseMethod);

    @Query(value = "EXEC FindBySoldShoppingCartId :soldShoppingCartID", nativeQuery = true)
   List< ShoppingCartDetails> findBySoldShoppingCartId(long soldShoppingCartID);

}