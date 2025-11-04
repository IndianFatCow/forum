package org.example.forum_platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// 评论实体
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime = LocalDateTime.now();

    private Integer likes = 0;

    private Integer dislikes = 0;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties({"posts", "comments"}) // 忽略 User 的双向关联
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"comments"}) // 忽略 Post 的双向关联
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties({"replies"}) // 避免 parent → replies 无限嵌套
    private Comment parent; // 回复的评论

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"parent"}) // 避免 replies → parent 无限嵌套
    private List<Comment> replies;


    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

}