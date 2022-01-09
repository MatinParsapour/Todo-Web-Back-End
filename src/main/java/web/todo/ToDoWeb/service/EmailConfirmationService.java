package web.todo.ToDoWeb.service;
public interface EmailConfirmationService {

    void sendEmail(String to, String messageText);

}
