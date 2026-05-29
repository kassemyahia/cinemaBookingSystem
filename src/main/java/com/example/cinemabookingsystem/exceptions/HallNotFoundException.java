package com.example.cinemabookingsystem.exceptions;

public class HallNotFoundException extends RuntimeException {
    public HallNotFoundException() {
        super("hall not found");
    }
}
