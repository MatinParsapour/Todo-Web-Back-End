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
     * @return the user saved in database
     * @throws Exception the exception for encrypting password
     */
    User saveDTO(UserSignUpDTO userSignUpDTO) throws Exception;

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
     * Insert a folder to list of user folders
     * @param userDTO the user include id
     * @param folderName name of the folder
     */
    void addFolder(UserDTO userDTO, String folderName);

    /**
     * Check if name of folder is doplicate or not
     * @param folderName the name of folder provided
     * @return true if name of folder already exists
     */
    Boolean existsByToDoFolderName(String folderName, String username);

    /**
     * Check if the username provided belong to a user
     * And then return all of the user to do folders
     * @param username the username provided
     * @return all to do folders
     */
    Set<ToDoFolder> getUserFolders(String username);

    /**
     * Check if user has folder with name provided
     * If name is wrong throw an exception
     * @param username username of user
     * @param toDoFolderName name of folder
     * @return a Set of todoFolders that is folder name is ok returns one todofolder else throw an exception
     */
    Set<ToDoFolder> getToDoFolder(String username, String toDoFolderName);

}
