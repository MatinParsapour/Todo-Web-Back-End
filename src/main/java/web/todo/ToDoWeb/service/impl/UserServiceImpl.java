package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.*;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.EmailValidation;
import web.todo.ToDoWeb.service.FilledValidation;
import web.todo.ToDoWeb.service.UniqueValidation;
import web.todo.ToDoWeb.service.UserService;
import web.todo.ToDoWeb.util.AES;

import java.time.LocalDate;
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

    public User saveDTO(UserSignUpDTO userSignUpDTO) throws Exception {
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
            throw new InValidException("The email isn't valid");
        }
        if (!passwordStrengthValidation(userSignUpDTO.getPassword())){
            throw new WeakException("The password provided is weak");
        }
        if (!dateIsValid(userSignUpDTO.getBirthDay())){
            throw new InValidException("The birthday provided isn't valid");
        }

        User user = new User();
        user.setFirstName(userSignUpDTO.getFirstName());
        user.setUserName(userSignUpDTO.getUserName());
        user.setPassword(AES.encrypt(userSignUpDTO.getPassword()));
        user.setEmail(userSignUpDTO.getEmail());
        user.setBirthDay(userSignUpDTO.getBirthDay());
        return save(user);
    }


    @Override
    public User updateDTO(UserDTO userDTO) throws Exception {
        if (findById(userDTO.getId()).isPresent()){
            User user = findById(userDTO.getId()).get();
            if(!user.getUserName().equals(userDTO.getUserName()) && existsByUserName(userDTO.getUserName())){
                throw new DoplicateException("The username is doplicate");
            }
            if (!user.getEmail().equals(userDTO.getEmail()) && existsByEmail(userDTO.getEmail())){
                throw new DoplicateException("The email is doplicate");
            }
            if (isNull(userDTO.getUserName()) || isNull(userDTO.getFirstName()) || isNull(userDTO.getEmail()) || isNull(userDTO.getBirthDay())){
                throw new EmptyException("Check the form, one or more fields are null");
            }
            if (isEmpty(userDTO.getUserName()) || isEmpty(userDTO.getFirstName()) || isEmpty(userDTO.getEmail()) || isEmpty(userDTO.getBirthDay())){
                throw new EmptyException("Check the form, one or more fields are empty");
            }
            if (isBlank(userDTO.getUserName()) || isBlank(userDTO.getFirstName()) || isBlank(userDTO.getEmail()) || isBlank(userDTO.getBirthDay())){
                throw new EmptyException("Check the form, one or more fields are blank");
            }
            if (isWhiteSpace(userDTO.getUserName()) || isWhiteSpace(userDTO.getFirstName()) || isWhiteSpace(userDTO.getEmail()) || isWhiteSpace(userDTO.getBirthDay())){
                throw new EmptyException("Check the form, one or more fields are white space");
            }
            if(!isEmailValid(userDTO.getEmail())){
                throw new InValidException("The email isn't valid");
            }
            if (!dateIsValid(userDTO.getBirthDay())){
                throw new InValidException("The birthday provided isn't valid");
            }
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            user.setBirthDay(userDTO.getBirthDay());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setIsDeleted(false);
            return save(user);
        } else {
            throw new NotFoundException("No user found");
        }
    }

    @Override
    public void deleteDTO(UserDTO userDTO) {
        if(findById(userDTO.getId()).isPresent()){
            User user = findById(userDTO.getId()).get();
            user.setIsDeleted(true);
            save(user);
        }else {
            throw new NotFoundException("No user found");
        }
    }

    @Override
    public UserDTO logInUser(UserSignUpDTO userSignUpDTO) throws Exception {
        return userRepository.findByUserNameAndPasswordAndIsDeletedFalse(userSignUpDTO.getUserName(), AES.encrypt(userSignUpDTO.getPassword()));
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
    public Boolean passwordStrengthValidation(String password) {
        Pattern pattern = Pattern.compile("^(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[0-9])+)(?=(.*[!@#$%^&*()\\-__+.])+).{8,}$");
        Matcher matcher =  pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public Boolean dateIsValid(String birthday) {
        LocalDate birthDate = LocalDate.parse(birthday);
        return birthDate.isBefore(LocalDate.now());
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
