package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.model.User;

import java.util.List;

public interface RequestRepository extends MongoRepository<Request, String> {

    List<Request> findAllByUserAndIsDeletedFalse(User user);

    Boolean existsByIdAndIsDeletedFalse(String requestId);
}
