package org.example.forum_platform.service;

import org.example.forum_platform.entity.Comment;
import org.example.forum_platform.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void likeComment(Long commentId) {
        commentRepository.findById(commentId).ifPresent(c -> {
            c.setLikes(c.getLikes() + 1);
            commentRepository.save(c);
        });
    }

    public void dislikeComment(Long commentId) {
        commentRepository.findById(commentId).ifPresent(c -> {
            c.setDislikes(c.getDislikes() + 1);
            commentRepository.save(c);
        });
    }
}