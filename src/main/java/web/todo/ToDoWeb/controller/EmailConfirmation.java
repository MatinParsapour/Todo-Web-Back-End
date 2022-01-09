package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.todo.ToDoWeb.service.UserService;

@RestController
@RequestMapping("/email")
public class EmailConfirmation {

    private final UserService userService;

    @Autowired
    public EmailConfirmation(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get code when user wants to sign up and check if code is correct
     * @param code the code generated for user to validate email
     * @throws Exception for encrypting password
     */
    @PostMapping("/check-code")
    public void checkCodeForSignUp(int code) throws Exception {
        userService.checkCode(code);
        userService.saveUser();
    }

    /**
     * Get email and if email is in database send a validation code to change password
     * @param email for user
     */
    @PostMapping("/forget-password")
    public void forgetPassword(String email){
        userService.checkEmail(email);
    }

    /**
     * After user provide email when forget password the code generated for
     * user will come here and check if code is correct
     * @param code the code generated for user
     */
    @PostMapping("/check-code-for-log-in")
    public void checkCodeForLogIn(int code){
        userService.checkCode(code);
    }

    /**
     * After code
     * user enter two password that must be the same else throw an exception
     * @param onePassword password of first input
     * @param secondPassword password of second input
     * @throws Exception for encrypting password
     */
    @PostMapping("/change-password")
    public void changePassword(String onePassword, String secondPassword) throws Exception {
        userService.changePassword(onePassword, secondPassword);
    }
}
