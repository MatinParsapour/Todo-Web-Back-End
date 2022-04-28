package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Comment;
import web.todo.ToDoWeb.model.dto.CommentDTO;
import web.todo.ToDoWeb.repository.CommentRepository;
import web.todo.ToDoWeb.service.CommentService;
import web.todo.ToDoWeb.service.ToDoService;
import web.todo.ToDoWeb.service.UserService;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment, String, CommentRepository>
        implements CommentService {

    private final UserService userService;
    private final ToDoService toDoService;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, UserService userService, ToDoService toDoService) {
        super(repository);
        this.userService = userService;
        this.toDoService = toDoService;
    }

    @Override
    public void addComment(CommentDTO comment) {
        Comment newComment = createCommentByCommentDTO(comment);
        save(newComment);
        toDoService.addCommentToComments(newComment, comment.getTodoId());
    }

    private Comment createCommentByCommentDTO(CommentDTO comment) {
        return Comment.builder()
                .user(userService.findById(comment.getUserId()).get())
                .message(comment.getMessage())
                .build();
    }
}
