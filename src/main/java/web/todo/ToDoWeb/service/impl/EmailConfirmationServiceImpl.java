package web.todo.ToDoWeb.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.service.EmailConfirmationService;

import java.util.Random;

@Service
@AllArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private static int code;
    private final JavaMailSender sender;

    public static int getCode() {
        return code;
    }

    static void setCodeZero(){code = null;}


    @Override
    public void sendEmail(String to, String messageText) {
        createCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("matin.parsapour.iam@gmail.com");
        message.setTo(to);
        String text = messageText + code;
        message.setText(text);
        message.setSubject("Confirm your email");
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
