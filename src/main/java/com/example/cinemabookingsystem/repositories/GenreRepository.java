package com.example.cinemabookingsystem.repositories;

import com.example.cinemabookingsystem.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}