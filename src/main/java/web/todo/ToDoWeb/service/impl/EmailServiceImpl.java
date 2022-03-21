package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.exception.InValidException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.CacheService;
import web.todo.ToDoWeb.service.SendEmailService;
import web.todo.ToDoWeb.service.EmailService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements EmailService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");

    private final UserRepository userRepository;
    private final CacheService cacheService;
    private final SendEmailService sendEmailService;
    private User user;

    public EmailServiceImpl(UserRepository repository, UserRepository userRepository, CacheService cacheService, SendEmailService sendEmailService) {
        super(repository);
        this.userRepository = userRepository;
        this.cacheService = cacheService;
        this.sendEmailService = sendEmailService;
    }


    @Override
    public void saveAndSendEmail(String userId, String newEmail) throws MessagingException, UnsupportedEncodingException {
        if (userId.isEmpty() || userId.isBlank()){
            throw new EmptyException("you must enter value for username");
        }
        if (newEmail.isEmpty() || newEmail.isBlank()){
            throw new EmptyException("you must enter value for email");
        }
        if (!userRepository.findByIdAndIsDeletedFalse(userId).isPresent()){
            throw new NotFoundException("No user found for " + userId);
        }
        user = userRepository.findByIdAndIsDeletedFalse(userId).get();
        user.setEmail(newEmail);
        sendEmailService.sendResetEmail(newEmail);
    }

    @Override
    public Boolean validateAndChangeEmail(String email, String code) {
        String emailCode = cacheService.getEmailCode(email);
        if (!emailCode.equals(code)){
            throw new InValidException("The code is invalid");
        }
        if (email.equals(user.getEmail())) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void sendCustomEmail(String from, String to, String message) throws MessagingException {
        if (!EMAIL_PATTERN.matcher(from).matches()){
            throw new InValidException("The email structure is invalid");
        }
        if (!EMAIL_PATTERN.matcher(to).matches()){
            throw new InValidException("The email structure is invalid");
        }
        sendEmailService.sendEmailFromCustomOrigin(from, to, message);
    }
}
