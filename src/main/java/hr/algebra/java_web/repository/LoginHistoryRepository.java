package hr.algebra.java_web.repository;


import hr.algebra.java_web.model.LoginHistory;
import hr.algebra.java_web.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {




}