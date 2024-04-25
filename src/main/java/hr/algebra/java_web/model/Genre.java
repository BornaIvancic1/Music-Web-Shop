package hr.algebra.java_web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Genre")
public class Genre {
    @jakarta.persistence.Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

}