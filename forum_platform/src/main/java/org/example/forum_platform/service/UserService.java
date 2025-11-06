package org.example.forum_platform.service;

import org.example.forum_platform.dto.RegisterRequest;
import org.example.forum_platform.dto.UserUpdateDTO;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 注册用户
    public User register(RegisterRequest request) {
        // 验证必要字段
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }

        // 检查用户名是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在: " + request.getUsername());
        }

        // 创建并设置用户信息
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setPoints(0);
        user.setLevel(1);

        // 设置可选字段
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail().trim());
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone(request.getPhone().trim());
        }

        return userRepository.save(user);
    }

    // 登录验证
    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    // 新增：安全更新用户资料的方法
    public User updateUserProfile(Long id, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + id));

        // 验证新用户名是否已存在
        if (updateDTO.getUsername() != null &&
                !updateDTO.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(updateDTO.getUsername()).isPresent()) {
                throw new RuntimeException("用户名已存在: " + updateDTO.getUsername());
            }
            user.setUsername(updateDTO.getUsername().trim());
        }

        // 验证邮箱是否已存在
        if (updateDTO.getEmail() != null &&
                !updateDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(updateDTO.getEmail()).isPresent()) {
                throw new RuntimeException("邮箱已被注册: " + updateDTO.getEmail());
            }
            user.setEmail(updateDTO.getEmail().trim());
        }

        // 验证手机号是否已存在
        if (updateDTO.getPhone() != null &&
                !updateDTO.getPhone().equals(user.getPhone())) {
            if (userRepository.findByPhone(updateDTO.getPhone()).isPresent()) {
                throw new RuntimeException("手机号已被注册: " + updateDTO.getPhone());
            }
            user.setPhone(updateDTO.getPhone().trim());
        }

        // 更新头像
        if (updateDTO.getAvatar() != null) {
            user.setAvatar(updateDTO.getAvatar().trim());
        }

        return userRepository.save(user);
    }


    // 更新资料
    public User updateProfile(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 查询用户积分
    public Integer getUserPoints(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + id));

        return user.getPoints();
    }

}