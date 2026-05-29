package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.CommentRequestDto;
import com.example.cinemabookingsystem.dtos.responses.CommentResponseDto;
import com.example.cinemabookingsystem.dtos.updates.CommentUpdateDto;
import com.example.cinemabookingsystem.exceptions.CommentNotFoundException;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.mappers.CommentMapper;
import com.example.cinemabookingsystem.repositories.CommentRepository;
import com.example.cinemabookingsystem.repositories.MovieRepository;
import com.example.cinemabookingsystem.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@AllArgsConstructor
@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final EntityManager entityManager;


    public List<CommentResponseDto> getComments() {
        return commentRepository.findAllWithMovieAndUser().stream().map(commentMapper::toResponseDto).toList();
    }

    public CommentResponseDto getCommentById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toResponseDto)
                .orElseThrow(CommentNotFoundException::new);
    }

    public List<CommentResponseDto> getCommentByUserId(Long id) {
        return commentRepository.findByUserId(id).orElseThrow(CommentNotFoundException::new)
                .stream().map(commentMapper::toResponseDto).toList();
    }

    public List<CommentResponseDto> getCommentByMovieId(Long id) {
        return commentRepository.findByMovieId(id).orElseThrow(CommentNotFoundException::new)
                .stream().map(commentMapper::toResponseDto).toList();
    }

    public CommentResponseDto createComment(CommentRequestDto commentRequest) {
        var movie = movieRepository.findById(commentRequest.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        var user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var comment = commentMapper.toEntity(commentRequest);
        comment.setMovie(movie);
        comment.setUser(user);
        comment = commentRepository.saveAndFlush(comment);
        entityManager.refresh(comment);
        return commentMapper.toResponseDto(comment);
    }

    public CommentResponseDto updateComment(Long commentId, CommentUpdateDto commentUpdate) {
        var comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentMapper.updateEntity(commentUpdate, comment);
        var savedComment = commentRepository.saveAndFlush(comment);
        entityManager.refresh(savedComment);
        return commentMapper.toResponseDto(savedComment);

    }

    public void deleteComment(Long id) {

        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException();
        }
        commentRepository.deleteById(id);
    }
}
