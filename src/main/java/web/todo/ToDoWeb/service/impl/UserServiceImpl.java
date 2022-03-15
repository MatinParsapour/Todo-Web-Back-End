package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.todo.ToDoWeb.exception.*;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserLoginDTO;
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
        notExistByUsernameAssertion(userSignUpDTO.getUserName());
        notExistByEmailAssertion(userSignUpDTO.getEmail());

        this.userSignUpDTO = userSignUpDTO;
        emailConfirmationService.sendEmailToVerifyUser(userSignUpDTO.getEmail());
    }

    @Override
    public User saveUser(String email) throws Exception {
        notEqualityAssertion(email, userSignUpDTO.getEmail());
        User user = initializeUserByUserSignUpDTO(userSignUpDTO);
        return save(user);
    }

    @Override
    public void signInUser(UserSignUpDTO userSignUpDTO) throws Exception {
        if (!existsByEmail(userSignUpDTO.getEmail())) {
            User user = initializeUserByUserSignUpDTO(userSignUpDTO);
            save(user);
        }
    }

    @Override
    public User updateDTO(UserDTO userDTO) {
        if (findById(userDTO.getId()).isPresent()) {
            User user = findById(userDTO.getId()).get();

            notExistByUsernameAssertion(userDTO.getUserName());
            notExistByEmailAssertion(userDTO.getEmail());

            notEqualityAssertion(user.getUserName(), userDTO.getUserName());
            notEqualityAssertion(user.getEmail(), userDTO.getEmail());

            validDateAssertion(userDTO.getBirthDay());

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
    public UserDTO logInUser(UserLoginDTO userLoginDTO) throws Exception {
        UserDTO savedUser = userRepository.findByUserNameAndPasswordAndIsDeletedFalse(userLoginDTO.getUserName(), AES.encrypt(userLoginDTO.getPassword()));
        UserSecurity.setUser(savedUser);
        return savedUser;
    }

    private void saveEmail(String email) {
        UserSignUpDTO userSignUpDTO = new UserSignUpDTO();
        userSignUpDTO.setEmail(email);
        this.userSignUpDTO = userSignUpDTO;
    }

    @Override
    public void changePassword(String email, String onePassword, String secondPassword) throws Exception {
        notEmptyAssertion(onePassword);
        notEmptyAssertion(secondPassword);

        strengthenAssertion(onePassword);

        notEqualityAssertion(onePassword, secondPassword);

        User user = userRepository.findByEmailAndIsDeletedFalse(email);
        user.setPassword(AES.encrypt(onePassword));
        save(user);
    }

    @Override
    public void validateEmailAndSendForgetPasswordEmail(String email) throws MessagingException, UnsupportedEncodingException {
        notEmptyAssertion(email);

        notExistByEmailAssertion(email);

        validEmailAssertion(email);

        saveEmail(email);
        emailConfirmationService.sendForgetPasswordEmail(email);
    }

    @Override
    public User getUserToDos(String toDoFolderName, String toDoListName, String username) {
        User user = userRepository.findByToDoFoldersToDoListsNameAndToDoFoldersNameAndUserNameAndIsDeletedFalse(toDoListName, toDoFolderName, username);
        user.getToDoFolders().forEach(element -> element.getToDoLists().removeIf(list -> !list.getName().equals(toDoListName)));
        return user;
    }

    @Override
    public UserDTO getUserDTOByUsername(String username) {
        User user = new User();
        UserDTO userDTO = new UserDTO();
        if (userRepository.findByUserNameAndIsDeletedFalse(username).isPresent()) {
            user = userRepository.findByUserNameAndIsDeletedFalse(username).get();
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
        return userRepository.findByUserNameAndIsDeletedFalse(username).get();
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws IOException {
        User user = userRepository.findByUserNameAndIsDeletedFalse(username).get();
        if (profileImage != null) {
            validImageTypeAssertion(profileImage);
            Path userFolder = Paths.get(USER_FOLDER + username).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + username + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(username + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(username));
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public void deleteProfile(String username) throws IOException {
        Files.deleteIfExists(Paths.get(USER_FOLDER + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION));
        User user = repository.findByUserNameAndIsDeletedFalse(username).get();
        user.setProfileImageUrl(null);
        save(user);
    }

    @Override
    public void deleteAccount(String username) {
        if (userRepository.findByUserNameAndIsDeletedFalse(username).isPresent()) {
            User user = userRepository.findByUserNameAndIsDeletedFalse(username).get();
            user.setIsDeleted(true);
            save(user);
        } else {
            throw new NotFoundException("No user found with this username");
        }
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

    private void notEmptyAssertion(String attribute) {
        if (isNull(attribute) || isBlank(attribute) || isWhiteSpace(attribute) || isEmpty(attribute)) {
            throw new EmptyException("The password provided is empty");
        }
    }

    private void strengthenAssertion(String attribute) {
        if (!passwordStrengthValidation(attribute)) {
            throw new WeakException("The password provided is weak");
        }
    }

    private void notEqualityAssertion(String firstSide, String secondSide) {
        if (!firstSide.equals(secondSide)) {
            throw new InValidException("The passwords don't match");
        }
    }

    private void notExistByEmailAssertion(String email) {
        if (existsByEmail(email)) {
            throw new DoplicateException("The email is doplicate");
        }
    }

    private void notExistByUsernameAssertion(String username) {
        if (existsByUserName(username)) {
            throw new DoplicateException("The username is doplicate");
        }
    }

    private void validEmailAssertion(String email) {
        if (!isEmailValid(email)) {
            throw new InValidException("The email isn't valid");
        }
    }

    private void validDateAssertion(String date) {
        if (!dateIsValid(date)) {
            throw new InValidException("The birthday provided isn't valid");
        }
    }

    private User initializeUserByUserSignUpDTO(UserSignUpDTO userSignUpDTO) throws Exception {
        User user = new User();
        user.setFirstName(userSignUpDTO.getFirstName());
        user.setUserName(userSignUpDTO.getUserName());
        user.setPassword(AES.encrypt(userSignUpDTO.getPassword()));
        user.setEmail(userSignUpDTO.getEmail());
        user.setLastName(userSignUpDTO.getLastName());
        return user;
    }

    private void validImageTypeAssertion(MultipartFile profileImage) {
        if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
            throw new IllegalStateException(profileImage.getOriginalFilename() + " is not a suitable file please upload image");
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmailAndIsDeletedFalse(email);
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return userRepository.existsByUserNameAndIsDeletedFalse(userName);
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
}
