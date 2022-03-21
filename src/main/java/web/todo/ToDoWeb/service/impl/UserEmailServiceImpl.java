package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Email;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.EmailRepository;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.UserEmailService;

import java.util.List;

@Service
public class UserEmailServiceImpl extends BaseServiceImpl<Email, String, EmailRepository> implements UserEmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    public UserEmailServiceImpl(EmailRepository repository, UserRepository userRepository) {
        super(repository);
        this.emailRepository = repository;
        this.userRepository = userRepository;
    }

    public void addNewEmail(String from, String to, String message){
        Email email = new Email();
        email.setOrigin(from);
        email.setDestination(to);
        email.setMessage(message);
        save(email);
    }

    @Override
    public List<Email> userInbox(String userId) {
        if (userRepository.findByIdAndIsDeletedFalse(userId).isPresent()){
            User user = userRepository.findByIdAndIsDeletedFalse(userId).get();
            return emailRepository.findAllByDestinationAndIsDeletedFalse(user.getEmail());
        } else {
            throw new NotFoundException("No user found");
        }
    }
}
