package com.zzz.spring_security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<Authority> authorities; // <== one person can have many authorities>
}
