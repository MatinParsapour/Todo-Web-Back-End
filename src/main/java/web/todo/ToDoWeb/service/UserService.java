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

    /**
     * Save data in database
     * Throws a doplicate exception if username or email is doplicate
     * Throws an empty exception if one or more of mandatory fields are empty
     * Throws an invalid exception if the email provided is invalid
     * @param userSignUpDTO the user with first name and last name and username and email and password
     * @throws Exception the exception for encrypting password
     */
    void saveDTO(UserSignUpDTO userSignUpDTO) throws Exception;

    /**
     * Save user based what temporary saved before
     * @return the user saved in database
     * @throws Exception for encrypting password
     */
    User saveUser(String email, String code) throws Exception;

    void validateEmailAndCode(String email, String code);

    UserDTO signInUser(UserSignUpDTO userSignUpDTO) throws Exception;

    /**
     * Update the user saved in data base
     * Throws a not found exception if the provided id doesn't exists
     * Throws an empty exception if one or more of mandatory fields are empty
     * Throws a doplicate exception if username or email if doplicate
     * Throws an invalid exception if email is invalid
     * @param userDTO the user with all of the field like phone number last name....
     * @return the user saved in database
     * @throws Exception the exception for encrypting password
     */
    UserDTO updateDTO(UserDTO userDTO) throws Exception;

    /**
     * Make the isDeleted field true
     * Throws a not found exception if the provided id doesn't exists
     * @param userDTO the user dto include first name and last name and .... which only use the id
     */
    void deleteDTO(UserDTO userDTO);

    /**
     * Find user by username and password to login
     * @param userLoginDTO the user include username and password
     * @return the User dto if the user found and not deleted
     * @throws Exception the exception for encrypting the password
     */
    UserDTO logInUser(UserLoginDTO userLoginDTO) throws Exception;

    /**
     * The method to connect ot data base and check if email is unique or not
     * @param email the email provided
     * @return true if user exists with the email provided
     */
    Boolean existsByEmail(String email);

    /**
     * Check if password is strong enough
     * @param password the password provided
     * @return true if the password is strong
     */
    Boolean passwordStrengthValidation(String password);

    /**
     * Check if birthday provided is valid or not
     * @param birthday the birthday provided
     * @return true if the birthday is parsable and is before now
     */
    Boolean dateIsValid(String birthday);

    /**
     * Get first input and second input password
     * and check them
     * @param onePassword password of first input
     * @param secondPassword password of second input
     * @throws Exception for encrypting password
     */
    void changePassword(String email, String onePassword, String secondPassword) throws Exception;

    /**
     * Check if email isn't null and exists in database
     * @param email the email user entered
     */
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
}
