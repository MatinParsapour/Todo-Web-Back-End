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

    Boolean existsByUserNameAndIsDeletedFalse(String userName);

    Boolean existsByValidatorEmailAndIsDeletedFalse(String email);

    User findByValidatorEmailAndIsDeletedFalse(String email);

    Boolean existsByEmailAndIsDeletedFalse(String email);

    Boolean existsByToDoFoldersNameAndUserNameAndIsDeletedFalse(String folderName, String userName);

    @Query(value = "{toDoFolders: {$elemMatch: {'to_do_lists.name': ?0, 'name': ?1}}, 'userName': ?2}")
    User existsByToDoFoldersToDoListsNameAndToDoFoldersNameAndUserNameAndIsDeletedFalse(String toDoListName,String toDoFolderName, String username);

    @Query(value = "{toDoFolders: {$elemMatch: {'to_do_lists.name': ?0, 'name': ?1}}, 'id': ?2}", fields = "{_id: 0, 'toDoFolders.$': 1}")
    User findByToDoFoldersToDoListsNameAndToDoFoldersNameAndIdAndIsDeletedFalse(String toDoListName,String toDoFolderName, String userId);

    UserDTO findByUserNameAndPasswordAndIsDeletedFalse(String username, String password);

    Optional<User> findByIdAndIsDeletedFalse(String userId);

    @Query(value = "{'userName': ?0, 'toDoFolders' : {$elemMatch: {name: ?1 }}}", fields = "{_id: 0, 'toDoLists.$': 1}")
    Optional<User> findByUserNameAndToDoFoldersNameAndIsDeletedFalse(String username, String todoFolderName);

    @Query(value = "{'userName': ?1, 'toDoFolders' : {$elemMatch: {name: ?0 }}}")
    Optional<User> findByToDoFoldersNameAndUserNameAndIsDeletedFalse(String toDoFolderName, String username);

    User findByEmailAndIsDeletedFalse(String email);
}
