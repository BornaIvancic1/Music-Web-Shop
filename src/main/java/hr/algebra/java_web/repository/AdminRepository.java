package hr.algebra.java_web.repository;

import hr.algebra.java_web.model.Admin;
import hr.algebra.java_web.model.JWUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query(value = "EXEC FindAdminByEmail:email", nativeQuery = true)
    Admin FindAdminByEmail(String email);

}
