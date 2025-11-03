package org.example.forum_platform.controller;

import org.example.forum_platform.entity.Post;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/posts")
    public List<Post> searchPosts(@RequestParam String keyword) {
        return searchService.searchPosts(keyword);
    }

    @GetMapping("/users")
    public List<User> searchUsers(@RequestParam String keyword) {
        return searchService.searchUsers(keyword);
    }
}