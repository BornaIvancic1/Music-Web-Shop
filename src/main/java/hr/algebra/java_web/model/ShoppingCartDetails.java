package hr.algebra.java_web.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ShoppingCartDetails")
public class ShoppingCartDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long soldShoppingCartID;

    private LocalDate purchaseDateTime;
    private String purchaseMethod;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSoldShoppingCartID() {
        return soldShoppingCartID;
    }

    public void setSoldShoppingCartID(Long soldShoppingCartID) {
        this.soldShoppingCartID = soldShoppingCartID;
    }



    public LocalDate getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(LocalDate purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }

    public String getPurchaseMethod() {
        return purchaseMethod;
    }

    public void setPurchaseMethod(String purchaseMethod) {
        this.purchaseMethod = purchaseMethod;
    }
}