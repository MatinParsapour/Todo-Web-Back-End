package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Email;
import web.todo.ToDoWeb.repository.EmailRepository;
import web.todo.ToDoWeb.service.UserEmailService;

@Service
public class UserEmailServiceImpl extends BaseServiceImpl<Email, String, EmailRepository> implements UserEmailService {

    private final EmailRepository emailRepository;

    public UserEmailServiceImpl(EmailRepository repository) {
        super(repository);
        this.emailRepository = repository;
    }

    public void addNewEmail(String from, String to, String message){
        Email email = new Email();
        email.setOrigin(from);
        email.setDestination(to);
        email.setMessage(message);
        save(email);
    }
}
