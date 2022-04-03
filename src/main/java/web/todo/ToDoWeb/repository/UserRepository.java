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

    Boolean existsByValidatorEmailAndIsDeletedFalse(String email);

    User findByValidatorEmailAndIsDeletedFalseAndIsBlockedFalse(String email);

    Boolean existsByEmailAndIsDeletedFalse(String email);

    Boolean existsByToDoFoldersNameAndIdAndIsDeletedFalse(String folderName, String userId);

    @Query(value = "{toDoFolders: {$elemMatch: {'to_do_lists.name': ?0, 'name': ?1}}, 'id': ?2}")
    User existsByToDoFoldersToDoListsNameAndToDoFoldersNameAndIdAndIsDeletedFalse(String toDoListName,String toDoFolderName, String userId);

    @Query(value = "{toDoFolders: {$elemMatch: {'to_do_lists.name': ?0, 'name': ?1}}, 'id': ?2}", fields = "{_id: 0, 'toDoFolders.$': 1}")
    User findByToDoFoldersToDoListsNameAndToDoFoldersNameAndIdAndIsDeletedFalse(String toDoListName,String toDoFolderName, String userId);

    UserDTO findByEmailAndPasswordAndIsDeletedFalse(String email, String password);

    UserDTO findByPhoneNumberAndPasswordAndIsDeletedFalse(long phoneNumber, String password);

    Optional<User> findByIdAndIsDeletedFalse(String userId);

    @Query(value = "{'id': ?0, 'toDoFolders' : {$elemMatch: {name: ?1 }}}", fields = "{_id: 0, 'toDoLists.$': 1}")
    Optional<User> findByIdAndToDoFoldersNameAndIsDeletedFalse(String userId, String todoFolderName);

    @Query(value = "{'id': ?1, 'toDoFolders' : {$elemMatch: {name: ?0 }}}")
    Optional<User> findByToDoFoldersNameAndIdAndIsDeletedFalse(String toDoFolderName, String userId);

    User findByEmailAndIsDeletedFalse(String email);

    User findByPhoneNumberAndIsDeletedFalse(Long phoneNumber);

    @Query(fields = "{_id: 0, firstName: 1, lastName: 1, email: 1}")
    List<User> findByEmailLikeAndIsDeletedFalseAndIsBlockedFalse(String email);
}
