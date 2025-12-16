package org.example.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String first_name;

    @Column
    private String last_name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String ssn;

    public User(String first_name, String last_name, String email, String password, String ssn) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.ssn = ssn;
    }

    public User() {

    }

    public UUID getId() {
        return id;
    }
}
