package web.todo.ToDoWeb.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailConfirmationService {

    void sendEmailToVerifyUser(String to) throws MessagingException, UnsupportedEncodingException;

    void sendForgetPasswordEmail(String to) throws MessagingException, UnsupportedEncodingException;

    void sendResetEmail(String to) throws MessagingException, UnsupportedEncodingException;
}
