package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Planned;

public interface PlannedRepository extends MongoRepository<Planned, String> {
}
