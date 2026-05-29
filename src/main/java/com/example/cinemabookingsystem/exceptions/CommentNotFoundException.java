package com.example.cinemabookingsystem.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("user not found any comment");
    }
}
