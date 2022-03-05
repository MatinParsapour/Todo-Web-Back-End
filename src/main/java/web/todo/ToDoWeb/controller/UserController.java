package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.service.UserService;
import web.todo.ToDoWeb.util.UserSecurity;

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
    public void addUser(@RequestBody UserSignUpDTO user) throws Exception {
        userService.saveDTO(user);
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


    @GetMapping("/get-to-dos/{toDoList}/{toDoFolder}/{username}")
    public User getToDos(@PathVariable("toDoList") String toDoList, @PathVariable("toDoFolder") String toDoFolder, @PathVariable("username") String username){
        return userService.getToDos(toDoFolder, toDoList, username);
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
    @PostMapping("/log-in")
    public UserDTO logInUser(@RequestBody UserSignUpDTO userSignUpDTO) throws Exception {
        return userService.logInUser(userSignUpDTO);
    }

    /**
     * Check if the username provided is unique or not
     * @param username the user username
     * @return true if user exists with the username provided
     */
    @GetMapping("/check-username/{username}")
    public Boolean checkUsername(@PathVariable("username") String username){
        return userService.existsByUserName(username);
    }

    /**
     * Check if the email provided is unique or not
     * @param email the user dto include email
     * @return true if user exists with the email provided
     */
    @GetMapping("/check-email/{email}")
    public Boolean checkEmail(@PathVariable("email") String email){
        return userService.existsByEmail(email);
    }

    @GetMapping("/get-user")
    public UserDTO getUser(){
        return UserSecurity.getUser();
    }
}
