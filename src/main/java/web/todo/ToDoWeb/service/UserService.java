package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;

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
}
