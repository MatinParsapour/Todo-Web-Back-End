package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.dto.CommentDTO;
import web.todo.ToDoWeb.service.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public void comment(@RequestBody @Valid CommentDTO comment){
        commentService.addComment(comment);
    }

    @DeleteMapping("/delete-comment/{commentId}/{todoId}")
    public void deleteComment(@PathVariable("commentId") String commentId, @PathVariable("todoId") String todoId){
        commentService.deleteComment(commentId, todoId);
    }
}
