package com.example.cinemabookingsystem.exceptions;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException() {
        super("genre not found");
    }
}
