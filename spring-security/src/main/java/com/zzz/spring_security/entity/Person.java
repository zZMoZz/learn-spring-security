package com.zzz.spring_security.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Person")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
}
