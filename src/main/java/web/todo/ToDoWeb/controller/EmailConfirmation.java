package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.dto.ChangePasswordDTO;
import web.todo.ToDoWeb.service.EmailService;
import web.todo.ToDoWeb.service.UserService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/email")
public class EmailConfirmation {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public EmailConfirmation(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    /**
     * Get code when user wants to sign up and check if code is correct
     * @param email user email
     * @throws Exception for encrypting password
     */
    @GetMapping("/verify-email/{email}")
    public void checkCodeForSignUp(@PathVariable("email") String email) throws Exception {
        userService.saveUser(email);
    }

    /**
     * Get email and if email is in database send a validation code to change password
     * @param email for user
     */
    @GetMapping("/forget-password/{email}")
    public void forgetPassword(@PathVariable("email") String email) throws MessagingException, UnsupportedEncodingException {
        userService.validateEmailAndSendForgetPasswordEmail(email);
    }

    /**
     * After code
     * user enter two password that must be the same else throw an exception
     * @param changePasswordDTO
     * @throws Exception for encrypting password
     */
    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) throws Exception {
        userService.changePassword(changePasswordDTO.getEmail(),changePasswordDTO.getPassword(), changePasswordDTO.getReTypePassword());
    }

    @PutMapping("/reset-email")
    public void resetEmail(@RequestParam("username") String username, @RequestParam("newEmail") String newEmail) throws MessagingException, UnsupportedEncodingException {
        emailService.saveAndSendEmail(username,newEmail);
    }

    @GetMapping("/validate-email/{email}")
    public Boolean validateEmail(@PathVariable("email") String email){
        return emailService.validateAndChangeEmail(email);
    }
}
