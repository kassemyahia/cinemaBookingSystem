package com.example.cinemabookingsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at",insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",insertable = false, updatable = false)
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovieGenre> movieGenres = new HashSet<>();

}
