package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);
}
