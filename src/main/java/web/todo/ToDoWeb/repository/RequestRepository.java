package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends MongoRepository<Request, String> {

    List<Request> findAllByUser(User user);

    Optional<Request> findByIdAndIsDeletedFalse(String requestId);
}
