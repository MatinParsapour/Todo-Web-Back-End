package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Comment;
import web.todo.ToDoWeb.model.ToDo;

import java.util.Optional;

public interface ToDoRepository extends MongoRepository<ToDo, String> {

    Optional<ToDo> findByComments(Comment comment);
}
