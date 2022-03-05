package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String> {

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    Boolean existsByToDoFoldersNameAndUserName(String folderName, String userName);

    @Query(value = "{toDoFolders: {$elemMatch: {'to_do_lists.name': ?0, 'name': ?1}}, 'userName': ?2}")
    User existsByToDoFoldersToDoListsNameAndToDoFoldersNameAndUserName(String toDoListName,String toDoFolderName, String username);

    @Query(value = "{toDoFolders: {$elemMatch: {'to_do_lists.name': ?0, 'name': ?1}}, 'userName': ?2}", fields = "{_id: 0, 'toDoFolders.$': 1}")
    User findByToDoFoldersToDoListsNameAndToDoFoldersNameAndUserName(String toDoListName,String toDoFolderName, String username);

    UserDTO findByUserNameAndPasswordAndIsDeletedFalse(String username, String password);

    Optional<User> findByUserName(String username);

    @Query(value = "{'userName': ?0, 'toDoFolders' : {$elemMatch: {name: ?1 }}}", fields = "{_id: 0, 'toDoLists.$': 1}")
    Optional<User> findByUserNameAndToDoFoldersName(String username, String todoFolderName);

    @Query(value = "{'userName': ?1, 'toDoFolders' : {$elemMatch: {name: ?0 }}}")
    Optional<User> findByToDoFoldersNameAndUserName(String toDoFolderName, String username);

    User findByEmail(String email);
}
