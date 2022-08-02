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
public class SendEmailController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public SendEmailController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/verify-email/{email}/{code}")
    public void checkCodeForSignUp(@PathVariable("email") String email, @PathVariable("code") String code) throws Exception {
        userService.saveUser(email, code);
    }

    @GetMapping("/verify-email-for-reset-password/{email}/{code}")
    public void verifyEmailForResetPassword(@PathVariable("email") String email, @PathVariable("code") String code) {
        userService.validateEmailAndCode(email, code);
    }

    @GetMapping("/forget-password/{email}")
    public void forgetPassword(@PathVariable("email") String email) throws MessagingException, UnsupportedEncodingException {
        userService.validateEmailAndSendForgetPasswordEmail(email);
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) throws Exception {
        userService.changePassword(changePasswordDTO.getEmail(), changePasswordDTO.getPassword(), changePasswordDTO.getReTypePassword());
    }

    @PutMapping("/reset-email")
    public void resetEmail(@RequestParam("userId") String userId, @RequestParam("newEmail") String newEmail) throws MessagingException, UnsupportedEncodingException {
        emailService.saveAndSendEmail(userId, newEmail);
    }

    @GetMapping("/validate-email/{email}/{code}")
    public Boolean validateEmail(@PathVariable("email") String email, @PathVariable("code") String code) {
        return emailService.validateAndChangeEmail(email, code);
    }
}
