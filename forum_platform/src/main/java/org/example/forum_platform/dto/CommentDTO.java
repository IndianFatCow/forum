package org.example.forum_platform.dto;

public class CommentDTO {
    private Long postId;
    private Long userId;
    private String content;
    private Long parentId; // 可选

    // ===== Getter & Setter =====
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}
