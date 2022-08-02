package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Boolean existsByEmailAndIsDeletedFalse(String email);

    Boolean existsByUserNameAndIsDeletedFalse(String username);

    Optional<User> findByUserNameAndPasswordAndIsDeletedFalse(String username, String password);

    Boolean existsByPhoneNumberAndIsDeletedFalse(Long phoneNumber);

    User findByEmailAndIsDeletedFalseAndIsBlockedFalse(String email);

    Boolean existsByToDoFoldersNameAndIdAndIsDeletedFalse(String folderName, String userId);

    @Query(value = "{toDoFolders: {$elemMatch: {'to_do_lists.name': ?0, 'name': ?1}}, 'id': ?2}")
    User existsByToDoFoldersToDoListsNameAndToDoFoldersNameAndIdAndIsDeletedFalse(String toDoListName, String toDoFolderName, String userId);

    @Query(value = "{toDoFolders: {$elemMatch: {'to_do_lists.name': ?0, 'name': ?1}}, 'id': ?2}", fields = "{_id: 0, 'toDoFolders.$': 1}")
    User findByToDoFoldersToDoListsNameAndToDoFoldersNameAndIdAndIsDeletedFalse(String toDoListName, String toDoFolderName, String userId);

    Optional<User> findByUserNameAndIsDeletedFalse(String userId);

    @Query(value = "{'id': ?0, 'toDoFolders' : {$elemMatch: {name: ?1 }}}", fields = "{_id: 0, 'toDoLists.$': 1}")
    Optional<User> findByUserNameAndToDoFoldersNameAndIsDeletedFalse(String userId, String todoFolderName);

    @Query(value = "{'id': ?1, 'toDoFolders' : {$elemMatch: {name: ?0 }}}")
    Optional<User> findByToDoFoldersNameAndIdAndIsDeletedFalse(String toDoFolderName, String userId);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Optional<User> findByPhoneNumberAndIsDeletedFalse(Long phoneNumber);

    @Query(fields = "{_id: 0, firstName: 1, lastName: 1, email: 1}")
    List<User> findByEmailLikeAndIsDeletedFalseAndIsBlockedFalse(String email);

    @Query(fields = "{_id: 1, firstName: 1, lastName: 1, profileImageUrl: 1}")
    User findByToDos(ToDo todo);

    @Query(value = "{'userName': ?0}", fields = "{_id: 1,bio: 1 ,firstName: 1, lastName: 1, profileImageUrl: 1, birthDay: 1, age: 1}")
    Optional<UserDTO> getUserPersonalInfo(String userName);

    @Query(value = "{'userName': ?0}", fields = "{_id: 1,provider: 1, email: 1, phoneNumber: 1, password: 1, userName: 1, registerDate: 1, lastLoginDate: 1, isPhoneVisible: 1, isEmailVisible: 1}")
    Optional<UserDTO> getUserSecurityInfo(String userName);

    @Query(value = "{'userName': ?0}", fields = "{_id: 1, accessLevel: 1}")
    Optional<UserDTO> getUserAccountInfo(String userName);

    @Query("{$or: [{ 'userName' : { '$regex' : ?0 , $options: 'i'}},{ 'bio' : { '$regex' : ?1 , $options: 'i'}}]}")
    List<User> findByUserNameContainsOrBioContains(String username, String bio);

    Boolean existsByTagsAndUserName(Tag tag, String username);
}
