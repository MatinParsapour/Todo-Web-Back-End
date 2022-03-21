package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Email;

public interface EmailRepository extends MongoRepository<Email, String> {
}
