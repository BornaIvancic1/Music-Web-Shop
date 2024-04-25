package hr.algebra.java_web.repository;


import hr.algebra.java_web.model.ShoppingCart;
import hr.algebra.java_web.model.SoldShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SoldShoppingCartRepository extends JpaRepository<SoldShoppingCart, Long> {
    @Query(value = "EXEC GetSoldShoppingCartIdsByUserId :userId", nativeQuery = true)
    List<Long> findIdsByJWUserId(Long userId);

    @Query(value = "EXEC GetSoldShoppingCartItemsByUserId :userId", nativeQuery = true)
    List<SoldShoppingCart> findByJWUserId(Long userId);

    @Query(value = "EXEC FindByCustomerIdAndPurchaseDateBetween :userId,:startDate,:endDate", nativeQuery = true)
    List<SoldShoppingCart> findByCustomerIdAndPurchaseDateBetween(Long userId, LocalDate startDate,LocalDate endDate);

  @Query(value = "EXEC FindByPurchaseDateBetween :userId,:startDate,:endDate", nativeQuery = true)
    List<SoldShoppingCart> findByPurchaseDateBetween(LocalDate startDate,LocalDate endDate);



    @Query(value = "EXEC TransferToLocation @soldShoppingCartIds = :soldShoppingCartIds, @location = :location", nativeQuery = true)
    void transferToLocation(@Param("soldShoppingCartIds") String soldShoppingCartIds, @Param("location") String location);

}