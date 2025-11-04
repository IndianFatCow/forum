package org.example.forum_platform.controller;

import org.example.forum_platform.entity.Board;
import org.example.forum_platform.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;
    // 创建版块
    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardService.createBoard(board);
    }
    // 获取所有版块
    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }
    // 获取单个版块
    @GetMapping("/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoardById(id).orElse(null);
    }
    // 删除板块
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }
}