package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Request;

public interface RequestRepository extends MongoRepository<Request, String> {
}
