package org.example.forum_platform.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import java.util.List;

// 版块实体
@Entity
@Table(name = "boards")
public class Board {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToOne
    private User moderator; // 版主

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Post> posts;

    // ---- getter/setter ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getModerator() { return moderator; }
    public void setModerator(User moderator) { this.moderator = moderator; }

    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }
}
