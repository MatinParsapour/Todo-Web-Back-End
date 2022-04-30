package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Comment;
import web.todo.ToDoWeb.model.dto.CommentDTO;

public interface CommentService extends BaseService<Comment, String> {

    void addComment(CommentDTO comment);

    void deleteComment(String commentId, String todoId);

    void editComment(String commentId, String message);
}
