package com.zzz.spring_security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Authority")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "authority")
    private String authority;

    // It is better to create Many-to-Many relation. but we don't do that for simplicity
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Person person;


}
