package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

public interface UserRepository extends MongoRepository<User, String> {

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    UserDTO findByUserNameAndPasswordAndIsDeletedFalse(String username, String password);
}
