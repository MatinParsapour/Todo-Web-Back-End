package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;

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
    User saveUser() throws Exception;

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
    User updateDTO(UserDTO userDTO) throws Exception;

    /**
     * Make the isDeleted field true
     * Throws a not found exception if the provided id doesn't exists
     * @param userDTO the user dto include first name and last name and .... which only use the id
     */
    void deleteDTO(UserDTO userDTO);

    /**
     * Find user by username and password to login
     * @param userSignUpDTO the user include username and password
     * @return the User dto if the user found and not deleted
     * @throws Exception the exception for encrypting the password
     */
    UserDTO logInUser(UserSignUpDTO userSignUpDTO) throws Exception;

    /**
     * The method to connect to data base and check if username is unique or not
     * @param userName the username provided
     * @return true if user exists with the username provided
     */
    Boolean existsByUserName(String userName);

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
    void changePassword(String onePassword, String secondPassword) throws Exception;

    /**
     * check if the code entered by user is correct
     * @param code the code user entered
     */
    void checkCode(int code);

    /**
     * Check if email isn't null and exists in database
     * @param email the email user entered
     */
    void checkEmail(String email);

}
