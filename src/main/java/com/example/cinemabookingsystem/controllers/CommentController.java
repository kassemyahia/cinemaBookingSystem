package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.CommentRequestDto;
import com.example.cinemabookingsystem.dtos.responses.CommentResponseDto;
import com.example.cinemabookingsystem.dtos.updates.CommentUpdateDto;
import com.example.cinemabookingsystem.services.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/comments")
class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentResponseDto> getComments() {
        return commentService.getComments();
    }
    @GetMapping("/{id}")
    public CommentResponseDto getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @GetMapping("/users/{userId}")
    public List<CommentResponseDto> getCommentsByUserId(@PathVariable("userId") Long userId) {
        return commentService.getCommentByUserId(userId);
    }

    @GetMapping("/movies/{movieId}")
    public List<CommentResponseDto> getCommentsByMovieId(@PathVariable("movieId") Long movieId) {
        return commentService.getCommentByMovieId(movieId);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@Valid @RequestBody CommentRequestDto commentRequest, UriComponentsBuilder uriBuilder) {
        var comment = commentService.createComment(commentRequest);
        var uri = uriBuilder.path("/comments/{id}").buildAndExpand(comment.getId()).toUri();
        return ResponseEntity.created(uri).body(comment);
    }

    @PutMapping("/{id}")
    public CommentResponseDto updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateDto commentUpdate) {
        return commentService.updateComment(id, commentUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
