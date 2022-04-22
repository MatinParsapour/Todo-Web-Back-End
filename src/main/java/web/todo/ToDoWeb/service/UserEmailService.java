package web.todo.ToDoWeb.service;

import org.springframework.data.domain.Page;
import web.todo.ToDoWeb.model.Email;
import web.todo.ToDoWeb.model.User;

import java.util.List;

public interface UserEmailService extends BaseService<Email, String> {

    void addNewEmail(String from, String to, String message);

    Page<Email> userInbox(String userId, Integer pageNumber, Integer pageSize);

    Page<Email> userOutbox(String userId, Integer pageNumber , Integer pageSize);

    Email getEmailDetails(String emailId);

    void deleteEmail(String emailId);

    List<User> getRecommendation(String email);
}
