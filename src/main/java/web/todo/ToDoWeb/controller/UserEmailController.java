package web.todo.ToDoWeb.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.Email;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.service.UserEmailService;

import java.util.List;

@RestController
@RequestMapping("/user-email")
public class UserEmailController {

    private final UserEmailService userEmailService;

    public UserEmailController(UserEmailService userEmailService) {
        this.userEmailService = userEmailService;
    }


    @GetMapping("/inbox/{userId}/{pageNumber}/{pageSize}")
    public Page<Email> inbox(@PathVariable("userId") String userId, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize){
        return userEmailService.userInbox(userId, pageNumber, pageSize);
    }

    @GetMapping("/outbox/{userId}/{pageNumber}/{pageSize}")
    public Page<Email> outbox(@PathVariable("userId") String userId, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize){
        return userEmailService.userOutbox(userId, pageNumber, pageSize);
    }

    @GetMapping("/get-recommendation/{email}")
    public List<User> getRecommendation(@PathVariable("email") String email){
        return userEmailService.getRecommendation(email);
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
