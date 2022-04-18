package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.List;
import java.util.Set;

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

    @PostMapping("/sign-in")
    public UserDTO signInUser(@Valid @RequestBody UserSignUpDTO userSignUpDTO) throws Exception {
        return userService.signInUser(userSignUpDTO);
    }

    /**
     * Update the saved user
     * @param userDTO the user that has been saved in database with id
     * @return the user saved in database
     * @throws Exception the exception for encrypting database
     */
    @PutMapping("/update-user")
    public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return userService.updateDTO(userDTO);
    }


    @GetMapping("/get-to-dos/{toDoList}/{toDoFolder}/{userId}")
    public User getToDos(@PathVariable("toDoList") String toDoList, @PathVariable("toDoFolder") String toDoFolder, @PathVariable("userId") String userId){
        return userService.getUserToDos(toDoFolder, toDoList, userId);
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
     * Check if the email provided is unique or not
     * @param email the user dto include email
     * @return true if user exists with the email provided
     */
    @GetMapping("/check-email/{email}")
    public Boolean checkEmail(@PathVariable("email") String email){
        return userService.existsByEmail(email);
    }

    @GetMapping("/get-user/{userId}")
    public UserDTO getUser(@PathVariable("userId") String userId){
        return userService.getUserDTOById(userId);
    }
    
    @GetMapping("/get-user-for-user-management/{userId}")
    public UserDTO getUserForUserManagement(@PathVariable("userId") String userId){
        return userService.getUserDTOByIdForUserManagement(userId);
    }

    @PutMapping("/update-profile-image")
    public User updateProfileImage(@RequestParam("userId") String userId,
                                   @RequestParam("profileImage")MultipartFile profileImage) throws IOException {
        return userService.updateProfileImage(userId, profileImage);
    }

    @GetMapping(value = "/image/{userId}/{fileName}",produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("userId") String userId, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + userId + FORWARD_SLASH + fileName));
    }

    @DeleteMapping("/delete-profile-image/{userId}")
    public void deleteProfileImage(@PathVariable("userId") String userId) throws IOException {
        userService.deleteProfile(userId);
    }

    @DeleteMapping("/delete-account/{userId}")
    public void deleteAccount(@PathVariable("userId") String userId){
        userService.deleteAccount(userId);
    }

    @GetMapping("/get-all/{pageNumber}/{pageSize}")
    public Page<User> getAllUsers(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize){
        return userService.getAllUsers(pageNumber, pageSize);
    }

    @PutMapping("/remove-from-followings")
    public void removeFromFollowings(@RequestParam("userId") String userId, @RequestParam("followingId") String followingId){
        userService.removeFromFollowing(userId, followingId);
    }

    @PutMapping("/unfollow")
    public void unFollow(@RequestParam("userId") String userId, @RequestParam("followerId") String followerId){
        userService.unFollow(userId, followerId);
    }
}
