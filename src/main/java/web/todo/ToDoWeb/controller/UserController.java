package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserLoginDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

    @PostMapping("/add-user")
    public void addUser(@Valid @RequestBody UserSignUpDTO user) throws Exception {
        userService.saveDTO(user);
    }

    @PostMapping("/sign-in")
    public UserDTO signInUser(@Valid @RequestBody UserSignUpDTO userSignUpDTO) throws Exception {
        return userService.signInUser(userSignUpDTO);
    }

    @PutMapping("/update-user")
    public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return userService.updateDTO(userDTO);
    }


    @GetMapping("/get-to-dos/{toDoList}/{toDoFolder}/{userId}")
    public User getToDos(@PathVariable("toDoList") String toDoList, @PathVariable("toDoFolder") String toDoFolder, @PathVariable("userId") String userId) {
        return userService.getUserToDos(toDoFolder, toDoList, userId);
    }

    @DeleteMapping("/delete-user")
    public void deleteUser(@RequestBody UserDTO userDTO) {
        userService.deleteDTO(userDTO);
    }

    @PostMapping("/log-in")
    public UserDTO logInUser(@RequestBody UserLoginDTO userLoginDTO) throws Exception {
        return userService.logInUser(userLoginDTO);
    }

    @GetMapping("/check-email/{email}")
    public Boolean checkEmail(@PathVariable("email") String email) {
        return userService.existsByEmail(email);
    }

    @GetMapping("/check-username/{username}")
    public Boolean checkUserName(@PathVariable("username") String username) {
        return userService.existsByUsername(username);
    }

    @GetMapping("/get-user/{username}")
    public UserDTO getUser(@PathVariable("username") String username) {
        return userService.getUserDTOByUsername(username);
    }

    @GetMapping("/get-user-for-user-management/{userId}")
    public UserDTO getUserForUserManagement(@PathVariable("userId") String userId) {
        return userService.getUserDTOByIdForUserManagement(userId);
    }

    @PutMapping("/update-profile-image")
    public User updateProfileImage(@RequestParam("userId") String userId,
                                   @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        return userService.updateProfileImage(userId, profileImage);
    }

    @GetMapping(value = "/image/{userId}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("userId") String userId, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + userId + FORWARD_SLASH + fileName));
    }

    @DeleteMapping("/delete-profile-image/{userId}")
    public void deleteProfileImage(@PathVariable("userId") String userId) throws IOException {
        userService.deleteProfile(userId);
    }

    @DeleteMapping("/delete-account/{userId}")
    public void deleteAccount(@PathVariable("userId") String userId) {
        userService.deleteAccount(userId);
    }

    @GetMapping("/get-all/{pageNumber}/{pageSize}")
    public Page<User> getAllUsers(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return userService.getAllUsers(pageNumber, pageSize);
    }

    @PutMapping("/un-follow-user")
    public void unFollow(@RequestParam("username") String username, @RequestParam("followingUsername") String followingUsername) {
        userService.unFollowUser(username, followingUsername);
    }

    @GetMapping("/get-user-by-todoId/{todoId}")
    public User getUserByToDoId(@PathVariable("todoId") String todoId) {
        return userService.findUserByToDoId(todoId);
    }

    @PostMapping("/forget-username")
    public void forgetUsername(@RequestParam("emailOrPhoneNumber") String emailOrPhoneNumber) throws MessagingException, UnsupportedEncodingException {
        userService.forgetUsername(emailOrPhoneNumber);
    }

    @PostMapping("/forget-username-code")
    public void forgetUsernameCode(@RequestParam("code") String code, @RequestParam("emailOrPhoneNumber") String emailOrPhoneNumber) {
        userService.checkForgetUsernameCode(code, emailOrPhoneNumber);
    }

    @PostMapping("/change-username")
    public void changeUsername(@RequestParam("newUsername") String newUsername, @RequestParam("emailOrPhoneNumber") String emailOrPhoneNumber) {
        userService.changeUsername(newUsername, emailOrPhoneNumber);
    }

    @PutMapping("/follow-tag")
    public void followTag(@RequestParam("username") String username, @RequestParam("tagName") String tagName) {
        userService.followTag(username, tagName);
    }

    @GetMapping("/is-tag-followed/{username}/{tagName}")
    public boolean isTagFollowed(@PathVariable("username") String username, @PathVariable("tagName") String tagName) {
        return userService.isTagFollowed(username, tagName);
    }

    @PutMapping("/un-follow-tag")
    public void unFollowTag(@RequestParam("username") String username, @RequestParam("tagName") String tagName) {
        userService.unFollowTag(username, tagName);
    }

    @GetMapping("/get-followers/{username}")
    public List<User> getFollowers(@PathVariable("username") String username) {
        return userService.getFollowers(username);
    }

    @GetMapping("/get-followings/{username}")
    public List<User> getFollowings(@PathVariable("username") String username) {
        return userService.getFollowings(username);
    }

    @GetMapping("/get-tags/{username}")
    public List<Tag> getTags(@PathVariable("username") String username) {
        return userService.getTags(username);
    }
}
