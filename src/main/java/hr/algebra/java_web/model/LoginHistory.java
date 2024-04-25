package hr.algebra.java_web.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Login_History")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login_time")
    private Date loginTime;

    @Column(name = "ip_address")
    private String ipAddress;



    public LoginHistory() {
    }

    public LoginHistory(int userId, Date loginTime, String ipAddress) {
        this.userId = userId;
        this.loginTime = loginTime;
        this.ipAddress = ipAddress;
    }

}