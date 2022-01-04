package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.MyDay;
import web.todo.ToDoWeb.model.User;

import java.util.List;

public interface MyDayRepository extends MongoRepository<MyDay, String> {

    @Query(fields = "{'toDos': 1, id: 0}")
    List<Category> findAllByUser(User user);
}
