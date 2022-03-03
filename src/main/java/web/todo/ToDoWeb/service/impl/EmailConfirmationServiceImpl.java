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
                + "Thank you,<br>"
                + "Your company name.";

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

    private void createCode(){
        Random random = new Random();
        code = random.nextInt(99999);
        if (code == 0){
            createCode();
        }
    }

}
