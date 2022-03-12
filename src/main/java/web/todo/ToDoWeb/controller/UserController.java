package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserLoginDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static web.todo.ToDoWeb.constants.FileConstants.FORWARD_SLASH;
import static web.todo.ToDoWeb.constants.FileConstants.USER_FOLDER;

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
    public void addUser(@Valid @RequestBody UserSignUpDTO user) throws Exception {
        userService.saveDTO(user);
    }

    /**
     * Update the saved user
     * @param userDTO the user that has been saved in database with id
     * @return the user saved in database
     * @throws Exception the exception for encrypting database
     */
    @PutMapping("/update-user")
    public User updateUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return userService.updateDTO(userDTO);
    }


    @GetMapping("/get-to-dos/{toDoList}/{toDoFolder}/{username}")
    public User getToDos(@PathVariable("toDoList") String toDoList, @PathVariable("toDoFolder") String toDoFolder, @PathVariable("username") String username){
        return userService.getUserToDos(toDoFolder, toDoList, username);
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
     * @param userLoginDTO the user dto include username and password
     * @return the user dto if the user found and not deleted
     * @throws Exception the exception for encrypting password
     */
    @PostMapping("/log-in")
    public UserDTO logInUser(@RequestBody UserLoginDTO userLoginDTO) throws Exception {
        return userService.logInUser(userLoginDTO);
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

    @GetMapping("/get-user/{username}")
    public UserDTO getUser(@PathVariable("username") String username){
        return userService.getUserDTOByUsername(username);
    }

    @PutMapping("/update-profile-image")
    public User updateProfileImage(@RequestParam("username") String username,
                                   @RequestParam("profileImage")MultipartFile profileImage) throws IOException {
        return userService.updateProfileImage(username, profileImage);
    }

    @GetMapping(value = "/image/{username}/{fileName}",produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @DeleteMapping("/delete-profile-image/{username}")
    public void deleteProfileImage(@PathVariable("username") String username) throws IOException {
        userService.deleteProfile(username);
    }
}
