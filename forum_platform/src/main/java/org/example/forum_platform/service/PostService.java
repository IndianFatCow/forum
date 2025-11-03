package org.example.forum_platform.service;

import org.example.forum_platform.entity.Post;
import org.example.forum_platform.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 发布帖子
    public Post createPost(Post post) {
        post.setCreateTime(LocalDateTime.now());
        return postRepository.save(post);
    }

    // 编辑帖子
    public Post updatePost(Post post) {
        post.setUpdateTime(LocalDateTime.now());
        return postRepository.save(post);
    }

    // 删除帖子（逻辑删除）
    public void deletePost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(p -> {
            p.setDeleted(true);
            postRepository.save(p);
        });
    }

    public List<Post> getPostsByBoard(Long boardId) {
        return postRepository.findByBoardIdAndDeletedFalse(boardId);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
}
