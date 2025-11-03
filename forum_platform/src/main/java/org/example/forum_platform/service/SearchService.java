package org.example.forum_platform.service;

import org.example.forum_platform.entity.Post;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.repository.PostRepository;
import org.example.forum_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.findByUsernameContaining(keyword);
    }
}