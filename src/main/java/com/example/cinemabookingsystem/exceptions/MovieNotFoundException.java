package com.example.cinemabookingsystem.exceptions;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException() {
        super("movie not found");
    }
}
