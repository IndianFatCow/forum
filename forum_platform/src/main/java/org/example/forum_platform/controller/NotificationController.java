package org.example.forum_platform.controller;

import org.example.forum_platform.entity.Notification;
import org.example.forum_platform.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserNotifications(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> result = notificationService.getUserNotifications(userId)
                    .stream()
                    .map(n -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("message","success");
                        map.put("id", n.getId());
                        map.put("type", n.getType());
                        map.put("content", n.getContent());
                        map.put("isRead", n.getIsRead());
                        map.put("createTime", n.getCreateTime());
                        map.put("senderId", n.getSender() != null ? n.getSender().getId() : null);
                        return map;
                    })
                    .toList();

            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "获取通知失败: " + e.getMessage()
            ));
        }
    }



    // 发送通知（可用于管理员公告、私信、系统提醒）
    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> payload) {
        try {
            Long senderId = ((Number) payload.get("senderId")).longValue();
            Long receiverId = ((Number) payload.get("receiverId")).longValue();
            String type = (String) payload.get("type");
            String content = (String) payload.get("content");

            Notification n = notificationService.sendNotification(senderId, receiverId, type, content);

            // 仅返回指定字段
            Map<String, Object> response = Map.of(
                    "message","success",
                    "id", n.getId(),
                    "type", n.getType(),
                    "content", n.getContent(),
                    "isRead", n.getIsRead(),
                    "createTime", n.getCreateTime()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "通知发送失败: " + e.getMessage()
            ));
        }
    }


    // 标记为已读
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(Map.of("message", "标记为已读"));
    }

    // 删除通知
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }
}
