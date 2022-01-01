package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    UserDTO findByUserNameAndPasswordAndIsDeletedFalse(String username, String password);

    Boolean existsByToDoFoldersName(String folderName);

    Optional<User> findByUserName(String username);
}
