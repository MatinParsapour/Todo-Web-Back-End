package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Comment;
import web.todo.ToDoWeb.repository.CommentRepository;
import web.todo.ToDoWeb.service.CommentService;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment, String, CommentRepository>
        implements CommentService {

    public CommentServiceImpl(CommentRepository repository) {
        super(repository);
    }
}
