package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
