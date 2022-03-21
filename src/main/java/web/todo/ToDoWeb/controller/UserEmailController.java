package web.todo.ToDoWeb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
