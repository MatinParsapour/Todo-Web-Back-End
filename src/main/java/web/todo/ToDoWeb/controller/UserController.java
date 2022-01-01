package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create new user
     * @param user the new user include first name and last name and username and email and password
     * @return the user saved in database
     * @throws Exception the exception for encrypting password
     */
    @PostMapping("/add-user")
    public User addUser(@RequestBody UserSignUpDTO user) throws Exception {
        return userService.saveDTO(user);
    }

    /**
     * Update the saved user
     * @param userDTO the user that has been saved in database with id
     * @return the user saved in database
     * @throws Exception the exception for encrypting database
     */
    @PutMapping("/update-user")
    public User updateUser(@RequestBody UserDTO userDTO) throws Exception {
        return userService.updateDTO(userDTO);
    }

    /**
     * Delete the user
     * @param userDTO the user in database if not throws an exception
     */
    @DeleteMapping("/delete-user")
    public void deleteUser(@RequestBody UserDTO userDTO){
        userService.deleteDTO(userDTO);
    }

    /**
     * the method when user wants to log in
     * @param userSignUpDTO the user dto include username and password
     * @return the user dto if the user found and not deleted
     * @throws Exception the exception for encrypting password
     */
    @GetMapping("/log-in")
    public UserDTO logInUser(@RequestBody UserSignUpDTO userSignUpDTO) throws Exception {
        return userService.logInUser(userSignUpDTO);
    }
}
