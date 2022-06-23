package web.todo.ToDoWeb.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserLoginDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

public interface UserService extends BaseService<User, String> {

    void saveDTO(UserSignUpDTO userSignUpDTO) throws Exception;

    User saveUser(String email, String code) throws Exception;

    void validateEmailAndCode(String email, String code);

    UserDTO signInUser(UserSignUpDTO userSignUpDTO) throws Exception;

    UserDTO updateDTO(UserDTO userDTO) throws Exception;

    void deleteDTO(UserDTO userDTO);

    UserDTO logInUser(UserLoginDTO userLoginDTO) throws Exception;

    Boolean existsByEmail(String email);

    Boolean passwordStrengthValidation(String password);

    Boolean dateIsValid(String birthday);

    void changePassword(String email, String onePassword, String secondPassword) throws Exception;

    void validateEmailAndSendForgetPasswordEmail(String email) throws MessagingException, UnsupportedEncodingException;

    User getUserToDos(String toDoFolderName, String toDoListName, String userId);

    UserDTO getUserDTOById(String userId);

    User getUserById(String userId);

    User updateProfileImage(String userId, MultipartFile profileImage) throws IOException;

    void deleteProfile(String userId) throws IOException;

    void deleteAccount(String userId);

    Page<User> getAllUsers(Integer pageNumber, Integer pageSize);

    UserDTO getUserDTOByIdForUserManagement(String userId);

    void addToDoToUserToDos(ToDo todo, String userId);

    void removeFromToDos(String userId, String toDoId);

    void addToFollowers(User responder, User applicant);

    void addToFollowings(User responder, User applicant);

    void removeFromFollowing(String userId, String followingId);

    void unFollow(String userId, String followerId);

    User findUserByToDoId(String todoId);

    void addToSaved(ToDo toDo, String userId);

    Boolean existsByUsername(String username);
}
