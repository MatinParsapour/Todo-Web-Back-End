package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Email;

import java.util.List;

public interface UserEmailService extends BaseService<Email, String> {

    void addNewEmail(String from, String to, String message);


    List<Email> userInbox(String userId);

    List<Email> userOutbox(String userId);

    Email getEmailDetails(String emailId);

    void deleteEmail(String emailId);
}
