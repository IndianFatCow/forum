package org.example.forum_platform.service;

import org.example.forum_platform.dto.CommentDTO;
import org.example.forum_platform.entity.Comment;
import org.example.forum_platform.entity.Post;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.repository.CommentRepository;
import org.example.forum_platform.repository.PostRepository;
import org.example.forum_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.example.forum_platform.config.SecurityConfig;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    // 添加评论
    public Comment addComment(CommentDTO dto, UserDetails userDetails) {
       //从userDetails获取当前用户
        User author = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("帖子不存在"));


        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(dto.getContent());
        comment.setCreateTime(LocalDateTime.now());

        if (dto.getParentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("父评论不存在"));
            comment.setParent(parent);
        }

        return commentRepository.save(comment);
    }
    // 获取某个帖子下的所有评论
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }
    // 点赞评论
    public void likeComment(Long commentId) {
        commentRepository.findById(commentId).ifPresent(c -> {
            c.setLikes(c.getLikes() + 1);
            commentRepository.save(c);
        });
    }
    // 点踩评论
    public void dislikeComment(Long commentId) {
        commentRepository.findById(commentId).ifPresent(c -> {
            c.setDislikes(c.getDislikes() + 1);
            commentRepository.save(c);
        });
    }
}