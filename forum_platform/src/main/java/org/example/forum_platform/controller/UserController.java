package org.example.forum_platform.controller;

import org.example.forum_platform.entity.User;
import org.example.forum_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // 用户登录
    @PostMapping("/login")
    public Optional<User> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

    // 更新资料
    @PutMapping("/{id}")
    public User updateProfile(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateProfile(user);
    }

    // 查询用户
    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}