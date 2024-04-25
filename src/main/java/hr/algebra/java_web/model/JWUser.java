package hr.algebra.java_web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Entity
@Table(name = "JWUser")
public class JWUser {

    @jakarta.persistence.Id
    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "username")
        private String username;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "password_hash")
        private String passwordHash;

        @Column(name = "password_salt")
        private String passwordSalt;

        private String email;


    public JWUser() {
    }

    public JWUser(Long id, String username, String passwordHash, String passwordSalt, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.email = email;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
