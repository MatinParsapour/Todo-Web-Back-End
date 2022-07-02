package web.todo.ToDoWeb.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface SendEmailService {

    void sendEmailToVerifyUser(String to) throws MessagingException, UnsupportedEncodingException;

    void sendForgetPasswordEmail(String to) throws MessagingException, UnsupportedEncodingException;

    void sendResetEmail(String to) throws MessagingException, UnsupportedEncodingException;

    void sendEmailFromCustomOrigin(String from, String to, String message) throws MessagingException;

    void sendForgetUsernameEmail(String emailOrPhoneNumber) throws MessagingException, UnsupportedEncodingException;
}
