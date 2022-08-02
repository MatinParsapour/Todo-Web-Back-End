package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Comment;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.CommentDTO;
import web.todo.ToDoWeb.repository.CommentRepository;
import web.todo.ToDoWeb.service.CommentService;
import web.todo.ToDoWeb.service.TagService;
import web.todo.ToDoWeb.service.ToDoService;
import web.todo.ToDoWeb.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment, String, CommentRepository>
        implements CommentService {

    private final UserService userService;
    private final ToDoService toDoService;
    private final TagService tagService;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, UserService userService, ToDoService toDoService, TagService tagService) {
        super(repository);
        this.userService = userService;
        this.toDoService = toDoService;
        this.tagService = tagService;
    }

    @Override
    public void addComment(CommentDTO comment) {
        Comment newComment = createCommentByCommentDTO(comment);
        ToDo toDo = toDoService.findById(comment.getTodoId()).orElseThrow(() -> new NotFoundException("No todo found by " + comment.getTodoId()));
        User user = userService.findByUsername(comment.getUserId()).orElseThrow(() -> new NotFoundException("No user found by " + comment));
        findTags(toDo, comment.getMessage(), user);
        save(newComment);
        toDoService.addCommentToComments(newComment, comment.getTodoId());
    }

    @Override
    public void deleteComment(String commentId, String todoId) {
        toDoService.deleteCommentFromToDoComments(commentId, todoId);
        deleteById(commentId);
    }

    @Override
    public void editComment(String commentId, String message) {
        Comment comment = findById(commentId).get();
        comment.setMessage(message);
        ToDo todo = toDoService.getByComment(comment);
        User user = comment.getUser();
        findTags(todo, message, user);
        save(comment);
    }

    private Comment createCommentByCommentDTO(CommentDTO comment) {
        return Comment.builder()
                .user(userService.findById(comment.getUserId()).get())
                .message(comment.getMessage())
                .build();
    }

    private void findTags(ToDo todo, String section, User user) {
        Matcher match = Pattern.compile("#([^\\d&%$_-]\\S{2,49})\\b").matcher(section);
        while (match.find()) {
            String grouped = match.group();
            tagService.addToDoToTag(todo, grouped, user);
        }
    }
}
