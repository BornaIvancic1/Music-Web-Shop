package hr.algebra.java_web.repository;

import hr.algebra.java_web.model.JWUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JWUserRepository extends JpaRepository<JWUser, Long> {
    @Query(value = "EXEC FindUserByEmail:email", nativeQuery = true)
    JWUser FindUserByEmail(String email);

}
