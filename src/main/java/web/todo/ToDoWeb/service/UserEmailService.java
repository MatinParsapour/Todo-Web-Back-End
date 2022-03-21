package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Email;

public interface UserEmailService extends BaseService<Email, String> {

    void addNewEmail(String from, String to, String message);


}
