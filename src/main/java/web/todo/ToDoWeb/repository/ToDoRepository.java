package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.ToDo;

public interface ToDoRepository extends MongoRepository<ToDo, String> {
}
