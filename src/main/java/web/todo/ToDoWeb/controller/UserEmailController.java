package web.todo.ToDoWeb.controller;

import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.Email;
import web.todo.ToDoWeb.service.UserEmailService;

import java.util.List;

@RestController
@RequestMapping("/user-email")
public class UserEmailController {

    private final UserEmailService userEmailService;

    public UserEmailController(UserEmailService userEmailService) {
        this.userEmailService = userEmailService;
    }


    @GetMapping("/inbox/{userId}")
    public List<Email> inbox(@PathVariable("userId") String userId){
        return userEmailService.userInbox(userId);
    }

    @GetMapping("/outbox/{userId}")
    public List<Email> outbox(@PathVariable("userId") String userId){
        return userEmailService.userOutbox(userId);
    }

    @GetMapping("/email/{emailId}")
    public Email email(@PathVariable("emailId") String emailId){
        return userEmailService.getEmailDetails(emailId);
    }
    
    @DeleteMapping("/delete-email/{emailId}")
    public void deleteEmail(@PathVariable("emailId") String emailId){
        userEmailService.deleteEmail(emailId);
    }
}
