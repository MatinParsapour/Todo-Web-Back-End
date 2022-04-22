package web.todo.ToDoWeb.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Email;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.EmailRepository;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.UserEmailService;

import org.springframework.data.domain.Pageable;
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
    public Page<Email> userInbox(String userId, Integer pageNumber, Integer pageSize) {
        if (userRepository.findByIdAndIsDeletedFalse(userId).isPresent()){
            Pageable paging = PageRequest.of(pageNumber, pageSize);
            User user = userRepository.findByIdAndIsDeletedFalse(userId).get();
            return emailRepository.findAllByDestinationAndIsDeletedFalse(user.getEmail(), paging);
        } else {
            throw new NotFoundException("No user found");
        }
    }

    @Override
    public Page<Email> userOutbox(String userId, Integer pageNumber, Integer pageSize) {
        if (userRepository.findByIdAndIsDeletedFalse(userId).isPresent()){
            Pageable paging = PageRequest.of(pageNumber, pageSize);
            User user = userRepository.findByIdAndIsDeletedFalse(userId).get();
            return emailRepository.findAllByOriginAndIsDeletedFalse(user.getEmail(), paging);
        } else {
            throw new NotFoundException("No user found");
        }
    }

    @Override
    public Email getEmailDetails(String emailId) {
        Email email = emailRepository.findByIdAndIsDeletedFalse(emailId);
        if (email == null){
            throw new NotFoundException("No email found");
        }
        return email;
    }

    @Override
    public void deleteEmail(String emailId) {
        Email email = emailRepository.findByIdAndIsDeletedFalse(emailId);
        if (email == null){
            throw new NotFoundException("No email found");
        }
        email.setIsDeleted(true);
        save(email);
    }

    @Override
    public List<User> getRecommendation(String email) {
        return userRepository.findByEmailLikeAndIsDeletedFalseAndIsBlockedFalse(email);
    }
}
