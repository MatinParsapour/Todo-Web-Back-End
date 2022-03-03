package web.todo.ToDoWeb.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.service.EmailConfirmationService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@AllArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private static String email;
    private final JavaMailSender sender;


    public void sendEmailToVerifyUser(String email) throws MessagingException, UnsupportedEncodingException {
        String toAddress = email;
        String fromAddress = "matin.parsapour.iam@gmail.com";
        String senderName = "My company";
        String subject = "Please verify your registration";
        String content = "Hello dear,<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>";

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = "http://localhost:4200/validate-email?email=" + email ;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        sender.send(message);
        Thread thread = new Thread(new Timer());
        thread.start();
    }

    @Override
    public void sendForgetPasswordEmail(String to) throws MessagingException, UnsupportedEncodingException {
        String toAddress = to;
        String fromAddress = "matin.parsapour.iam@gmail.com";
        String senderName = "My company";
        String subject = "Please click on the link below";
        String content = "Hello dear,<br>"
                + "Please click the link below to change your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">CHANGE PASSWORD</a></h3>"
                + "Thank you,<br>";

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = "http://localhost:4200/reset-password?email=" + to ;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        sender.send(message);
    }

    public static void emptyEmailField(){
        email = null;
    }

}
