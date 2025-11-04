package org.example.forum_platform.controller;

import org.example.forum_platform.dto.CommentDTO;
import org.example.forum_platform.entity.Comment;
import org.example.forum_platform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    // 添加评论
    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentDTO dto, @AuthenticationPrincipal UserDetails userDetails) {//接收CommentDTO对象
        // userDetails 会从 Spring Security 自动注入当前登录用户信息
        Comment comment = commentService.addComment(dto, userDetails);
        return ResponseEntity.ok(comment);
    }
    // 获取某个帖子下的所有评论
    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }
    // 点赞评论
    @PostMapping("/{id}/like")
    public void likeComment(@PathVariable Long id) {
        commentService.likeComment(id);
    }
    // 点踩评论
    @PostMapping("/{id}/dislike")
    public void dislikeComment(@PathVariable Long id) {
        commentService.dislikeComment(id);
    }
}