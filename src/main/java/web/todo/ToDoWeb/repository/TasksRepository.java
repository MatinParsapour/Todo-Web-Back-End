package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Tasks;

public interface TasksRepository extends MongoRepository<Tasks, String> {
}
