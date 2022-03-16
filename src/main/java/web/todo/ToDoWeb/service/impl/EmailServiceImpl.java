package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.exception.InValidException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.CacheCodeService;
import web.todo.ToDoWeb.service.EmailConfirmationService;
import web.todo.ToDoWeb.service.EmailService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements EmailService {

    private final UserRepository userRepository;
    private final CacheCodeService cacheCodeService;
    private final EmailConfirmationService emailConfirmationService;
    private User user;

    public EmailServiceImpl(UserRepository repository, UserRepository userRepository, CacheCodeService cacheCodeService, EmailConfirmationService emailConfirmationService) {
        super(repository);
        this.userRepository = userRepository;
        this.cacheCodeService = cacheCodeService;
        this.emailConfirmationService = emailConfirmationService;
    }


    @Override
    public void saveAndSendEmail(String username, String newEmail) throws MessagingException, UnsupportedEncodingException {
        if (username.isEmpty() || username.isBlank()){
            throw new EmptyException("you must enter value for username");
        }
        if (newEmail.isEmpty() || newEmail.isBlank()){
            throw new EmptyException("you must enter value for email");
        }
        if (!userRepository.findByUserNameAndIsDeletedFalse(username).isPresent()){
            throw new NotFoundException("No user found for " + username);
        }
        user = userRepository.findByUserNameAndIsDeletedFalse(username).get();
        user.setEmail(newEmail);
        emailConfirmationService.sendResetEmail(newEmail);
    }

    @Override
    public Boolean validateAndChangeEmail(String email, String code) {
        String emailCode = cacheCodeService.getEmailCode(email);
        if (!emailCode.equals(code)){
            throw new InValidException("The code is invalid");
        }
        if (email.equals(user.getEmail())) {
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
