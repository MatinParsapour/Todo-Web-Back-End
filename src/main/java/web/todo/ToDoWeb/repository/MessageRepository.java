package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
