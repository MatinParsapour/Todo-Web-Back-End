package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService extends BaseService<User, String>{

    void saveAndSendEmail(String userId, String newEmail) throws MessagingException, UnsupportedEncodingException;

    Boolean validateAndChangeEmail(String email, String code);

    void sendCustomEmail(String from, String to, String message) throws MessagingException;
}
