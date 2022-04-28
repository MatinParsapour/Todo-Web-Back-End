package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Comment;
import web.todo.ToDoWeb.model.dto.CommentDTO;

public interface CommentService extends BaseService<Comment, String> {

    void addComment(CommentDTO comment);
}
