package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.todo.ToDoWeb.exception.*;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.*;
import web.todo.ToDoWeb.util.AES;
import web.todo.ToDoWeb.util.UserSecurity;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;
import static web.todo.ToDoWeb.constants.FileConstants.*;

@Service

public class UserServiceImpl extends BaseServiceImpl<User, String, UserRepository>
        implements UserService, UniqueValidation, FilledValidation, EmailValidation {

    private final UserRepository userRepository;
    private final EmailConfirmationService emailConfirmationService;
    private UserSignUpDTO userSignUpDTO;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserRepository userRepository, EmailConfirmationService emailConfirmationService) {
        super(repository);
        this.userRepository = userRepository;
        this.emailConfirmationService = emailConfirmationService;
    }

    public void saveDTO(UserSignUpDTO userSignUpDTO) throws Exception {
        if (existsByUserName(userSignUpDTO.getUserName())) {
            throw new DoplicateException("The username is doplicate");
        }
        if (existsByEmail(userSignUpDTO.getEmail())) {
            throw new DoplicateException("The email is doplicate");
        }
        if (isNull(userSignUpDTO.getUserName()) || isNull(userSignUpDTO.getFirstName()) || isNull(userSignUpDTO.getEmail()) || isNull(userSignUpDTO.getPassword()) || isNull(userSignUpDTO.getLastName())) {
            throw new EmptyException("Check the form, one or more fields are null");
        }
        if (isEmpty(userSignUpDTO.getUserName()) || isEmpty(userSignUpDTO.getFirstName()) || isEmpty(userSignUpDTO.getEmail()) || isEmpty(userSignUpDTO.getPassword()) || isEmpty(userSignUpDTO.getLastName())) {
            throw new EmptyException("Check the form, one or more fields are empty");
        }
        if (isBlank(userSignUpDTO.getUserName()) || isBlank(userSignUpDTO.getFirstName()) || isBlank(userSignUpDTO.getEmail()) || isBlank(userSignUpDTO.getPassword()) || isBlank(userSignUpDTO.getLastName())) {
            throw new EmptyException("Check the form, one or more fields are blank");
        }
        if (isWhiteSpace(userSignUpDTO.getUserName()) || isWhiteSpace(userSignUpDTO.getFirstName()) || isWhiteSpace(userSignUpDTO.getEmail()) || isWhiteSpace(userSignUpDTO.getPassword()) || isWhiteSpace(userSignUpDTO.getLastName())) {
            throw new EmptyException("Check the form, one or more fields are white space");
        }
        if (!isEmailValid(userSignUpDTO.getEmail())) {
            throw new InValidException("The email isn't valid");
        }
        if (!passwordStrengthValidation(userSignUpDTO.getPassword())) {
            throw new WeakException("The password provided is weak");
        }
        this.userSignUpDTO = userSignUpDTO;
        emailConfirmationService.sendEmailToVerifyUser(userSignUpDTO.getEmail());
    }

    @Override
    public User saveUser(String email) throws Exception {
        if (!email.equals(userSignUpDTO.getEmail())) {
            throw new InValidException("The email is invalid");
        }
        User user = new User();
        user.setFirstName(userSignUpDTO.getFirstName());
        user.setUserName(userSignUpDTO.getUserName());
        user.setPassword(AES.encrypt(userSignUpDTO.getPassword()));
        user.setEmail(userSignUpDTO.getEmail());
        user.setLastName(userSignUpDTO.getLastName());
        return save(user);
    }

    @Override
    public User updateDTO(UserDTO userDTO) throws Exception {
        if (findById(userDTO.getId()).isPresent()) {
            User user = findById(userDTO.getId()).get();
            if (!user.getUserName().equals(userDTO.getUserName()) && existsByUserName(userDTO.getUserName())) {
                throw new DoplicateException("The username is doplicate");
            }
            if (!user.getEmail().equals(userDTO.getEmail()) && existsByEmail(userDTO.getEmail())) {
                throw new DoplicateException("The email is doplicate");
            }
            if (isNull(userDTO.getUserName()) || isNull(userDTO.getFirstName()) || isNull(userDTO.getEmail()) || isNull(userDTO.getBirthDay())) {
                throw new EmptyException("Check the form, one or more fields are null");
            }
            if (isEmpty(userDTO.getUserName()) || isEmpty(userDTO.getFirstName()) || isEmpty(userDTO.getEmail()) || isEmpty(userDTO.getBirthDay())) {
                throw new EmptyException("Check the form, one or more fields are empty");
            }
            if (isBlank(userDTO.getUserName()) || isBlank(userDTO.getFirstName()) || isBlank(userDTO.getEmail()) || isBlank(userDTO.getBirthDay())) {
                throw new EmptyException("Check the form, one or more fields are blank");
            }
            if (isWhiteSpace(userDTO.getUserName()) || isWhiteSpace(userDTO.getFirstName()) || isWhiteSpace(userDTO.getEmail()) || isWhiteSpace(userDTO.getBirthDay())) {
                throw new EmptyException("Check the form, one or more fields are white space");
            }
            if (!isEmailValid(userDTO.getEmail())) {
                throw new InValidException("The email isn't valid");
            }
            if (!dateIsValid(userDTO.getBirthDay())) {
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
        if (findById(userDTO.getId()).isPresent()) {
            User user = findById(userDTO.getId()).get();
            user.setIsDeleted(true);
            save(user);
        } else {
            throw new NotFoundException("No user found");
        }
    }

    @Override
    public UserDTO logInUser(UserSignUpDTO userSignUpDTO) throws Exception {
        UserDTO savedUser = userRepository.findByUserNameAndPasswordAndIsDeletedFalse(userSignUpDTO.getUserName(), AES.encrypt(userSignUpDTO.getPassword()));
        UserSecurity.setUser(savedUser);
        return savedUser;
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
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public Boolean dateIsValid(String birthday) {
        LocalDate birthDate = LocalDate.parse(birthday);
        return birthDate.isBefore(LocalDate.now());
    }

    private void saveEmail(String email) {
        UserSignUpDTO userSignUpDTO = new UserSignUpDTO();
        userSignUpDTO.setEmail(email);
        this.userSignUpDTO = userSignUpDTO;
    }

    @Override
    public void changePassword(String email, String onePassword, String secondPassword) throws Exception {
        if (isNull(onePassword) || isBlank(onePassword) || isWhiteSpace(onePassword) || isEmpty(onePassword)) {
            throw new EmptyException("The password provided is empty");
        }
        if (isNull(secondPassword) || isBlank(secondPassword) || isWhiteSpace(secondPassword) || isEmpty(secondPassword)) {
            throw new EmptyException("The password provided is empty");
        }
        if (!passwordStrengthValidation(onePassword)) {
            throw new WeakException("The password provided is weak");
        }
        if (!onePassword.equals(secondPassword)) {
            throw new InValidException("The passwords don't match");
        }
        User user = userRepository.findByEmail(email);
        user.setPassword(AES.encrypt(onePassword));
        save(user);
    }

    @Override
    public void checkEmail(String email) throws MessagingException, UnsupportedEncodingException {
        if (isNull(email) || isBlank(email) || isWhiteSpace(email) || isEmpty(email)) {
            throw new EmptyException("The email is empty");
        }
        if (!existsByEmail(email)) {
            throw new NotFoundException("The email provided doesn't exists");
        }
        if (!isEmailValid(email)) {
            throw new InValidException("The email isn't valid");
        }
        saveEmail(email);
        emailConfirmationService.sendForgetPasswordEmail(email);
    }

    @Override
    public User getToDos(String toDoFolderName, String toDoListName, String username) {
        User user = userRepository.findByToDoFoldersToDoListsNameAndToDoFoldersNameAndUserName(toDoListName, toDoFolderName, username);
        user.getToDoFolders().forEach(element -> element.getToDoLists().removeIf(list -> !list.getName().equals(toDoListName)));
        return user;
    }

    @Override
    public UserDTO getUserDTOByUsername(String username) {
        User user = new User();
        UserDTO userDTO = new UserDTO();
        if (userRepository.findByUserName(username).isPresent()) {
            user = userRepository.findByUserName(username).get();
        }
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        if (user.getBirthDay() != null) {
            userDTO.setBirthDay(user.getBirthDay());
            userDTO.setAge(user.getAge());
        }
        if (user.getPhoneNumber() != null) {
            userDTO.setPhoneNumber(user.getPhoneNumber());
        }
        userDTO.setProfileImageUrl(user.getProfileImageUrl());
        return userDTO;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).get();
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws IOException {
        User user = userRepository.findByUserName(username).get();
        if (profileImage != null) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new IllegalStateException(profileImage.getOriginalFilename() + " is not a suitable file please upload image");
            }
            Path userFolder = Paths.get(USER_FOLDER + username).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                System.out.println(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + username + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(username + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(username));
            userRepository.save(user);
        }
        return user;
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
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
