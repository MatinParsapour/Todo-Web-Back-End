package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.MyDay;

public interface MyDayRepository extends MongoRepository<MyDay, String> {
}
