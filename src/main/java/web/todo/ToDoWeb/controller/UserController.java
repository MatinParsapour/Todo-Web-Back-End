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
}
