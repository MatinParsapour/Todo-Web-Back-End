package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.DoplicateException;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.exception.UnValidException;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.EmailValidation;
import web.todo.ToDoWeb.service.FilledValidation;
import web.todo.ToDoWeb.service.UniqueValidation;
import web.todo.ToDoWeb.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String, UserRepository>
        implements UserService,UniqueValidation, FilledValidation, EmailValidation {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    public User saveDTO(UserSignUpDTO userSignUpDTO){
        if(existsByUserName(userSignUpDTO.getUserName())){
            throw new DoplicateException("The username is doplicate");
        }
        if (existsByEmail(userSignUpDTO.getEmail())){
            throw new DoplicateException("The email is doplicate");
        }
        if (isNull(userSignUpDTO.getUserName()) || isNull(userSignUpDTO.getFirstName()) || isNull(userSignUpDTO.getEmail()) || isNull(userSignUpDTO.getPassword()) || isNull(userSignUpDTO.getBirthDay())){
            throw new EmptyException("Check the form, one or more fields are null");
        }
        if (isEmpty(userSignUpDTO.getUserName()) || isEmpty(userSignUpDTO.getFirstName()) || isEmpty(userSignUpDTO.getEmail()) || isEmpty(userSignUpDTO.getPassword()) || isEmpty(userSignUpDTO.getBirthDay())){
            throw new EmptyException("Check the form, one or more fields are empty");
        }
        if (isBlank(userSignUpDTO.getUserName()) || isBlank(userSignUpDTO.getFirstName()) || isBlank(userSignUpDTO.getEmail()) || isBlank(userSignUpDTO.getPassword()) || isBlank(userSignUpDTO.getBirthDay())){
            throw new EmptyException("Check the form, one or more fields are blank");
        }
        if (isWhiteSpace(userSignUpDTO.getUserName()) || isWhiteSpace(userSignUpDTO.getFirstName()) || isWhiteSpace(userSignUpDTO.getEmail()) || isWhiteSpace(userSignUpDTO.getPassword()) || isWhiteSpace(userSignUpDTO.getBirthDay())){
            throw new EmptyException("Check the form, one or more fields are white space");
        }
        if(!isEmailValid(userSignUpDTO.getEmail())){
            throw new UnValidException("The email isn't valid");
        }

        User user = new User();
        user.setFirstName(userSignUpDTO.getFirstName());
        user.setUserName(userSignUpDTO.getUserName());
        user.setPassword(userSignUpDTO.getPassword());
        user.setEmail(userSignUpDTO.getEmail());
        user.setBirthDay(userSignUpDTO.getBirthDay());
        return userRepository.save(user);
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean isEmpty(String field) {
        return field.isEmpty();
    }

    @Override
    public Boolean isBlank(String field) {
        return field.isBlank();
    }

    @Override
    public Boolean isNull(String field) {
        return field == null;
    }

    @Override
    public Boolean isWhiteSpace(String field) {
        return field.trim().isEmpty();
    }

    @Override
    public Boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
