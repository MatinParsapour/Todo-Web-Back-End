package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.todo.ToDoWeb.constants.Authority;
import web.todo.ToDoWeb.enumeration.Role;
import web.todo.ToDoWeb.exception.*;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserLoginDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.*;
import web.todo.ToDoWeb.util.AES;

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
import static web.todo.ToDoWeb.constants.Authority.*;
import static web.todo.ToDoWeb.constants.FileConstants.*;

@Service

public class UserServiceImpl extends BaseServiceImpl<User, String, UserRepository>
        implements UserService, UniqueValidation, FilledValidation, EmailValidation {

    private final UserRepository userRepository;
    private final SendEmailService sendEmailService;
    private final CacheService cacheService;
    private UserSignUpDTO userSignUpDTO;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserRepository userRepository, SendEmailService sendEmailService, CacheService cacheService) {
        super(repository);
        this.userRepository = userRepository;
        this.sendEmailService = sendEmailService;
        this.cacheService = cacheService;
    }

    public void saveDTO(UserSignUpDTO userSignUpDTO) throws Exception {
        notExistByEmailAssertion(userSignUpDTO.getEmail());

        this.userSignUpDTO = userSignUpDTO;
        sendEmailService.sendEmailToVerifyUser(userSignUpDTO.getEmail());
    }

    @Override
    public User saveUser(String email, String code) throws Exception {
        notEqualityAssertion(email, userSignUpDTO.getEmail());
        validateEmailAndCode(email, code);
        User user = initializeUserByUserSignUpDTO(userSignUpDTO);
        return save(user);
    }

    @Override
    public void validateEmailAndCode(String email, String code) {
        String emailCode = cacheService.getEmailCode(email);
        if (!emailCode.equals(code)) {
            throw new InValidException("The code is invalid");
        }
    }

    @Override
    public UserDTO signInUser(UserSignUpDTO userSignUpDTO) throws Exception {
        if (!userRepository.existsByValidatorEmailAndIsDeletedFalse(userSignUpDTO.getEmail())) {
            User user = initializeUserByUserSignUpDTO(userSignUpDTO);
            save(user);
            return initializeUserDTO(user);
        }
        User user = userRepository.findByValidatorEmailAndIsDeletedFalse(userSignUpDTO.getEmail());
        return initializeUserDTO(user);
    }

    @Override
    public UserDTO updateDTO(UserDTO userDTO) {
        if (findById(userDTO.getId()).isPresent()) {
            User user = findById(userDTO.getId()).get();

            if (!user.getEmail().equals(userDTO.getEmail())) {
                notExistByEmailAssertion(userDTO.getEmail());
            }

            validDateAssertion(userDTO.getBirthDay());

            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setBirthDay(userDTO.getBirthDay());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setIsBlocked(userDTO.getIsBlocked());
            changeRole(user, userDTO);
            user.setIsDeleted(userDTO.getIsDeleted());
            save(user);
            return initializeUserDTO(user);
        } else {
            throw new NotFoundException("No user found");
        }
    }

    private void changeRole(User user, UserDTO userDTO) {
        switch (userDTO.getRole().name()) {
            case "ROLE_SUPER_ADMIN":
                user.setRole(userDTO.getRole());
                user.setAuthorities(SUPER_ADMIN_AUTHORITY);
                break;
            case "ROLE_ADMIN":
                user.setRole(userDTO.getRole());
                user.setAuthorities(ADMIN_AUTHORITY);
                break;
            case "ROLE_MANAGER":
                user.setRole(userDTO.getRole());
                user.setAuthorities(MANAGER_AUTHORITY);
                break;
            case "ROLE_USER":
                user.setRole(userDTO.getRole());
                user.setAuthorities(USER_AUTHORITY);
                break;
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
        UserDTO user;
        try {
            long phoneNumber = Long.parseLong(userLoginDTO.getEmailOrPhone());
            user = userRepository.findByPhoneNumberAndPassword(phoneNumber, AES.encrypt(userLoginDTO.getPassword()));
        } catch (NumberFormatException exception) {
            user = userRepository.findByEmailAndPassword(userLoginDTO.getEmailOrPhone(), AES.encrypt(userLoginDTO.getPassword()));
        }
        if (user == null) {
            findCredentialsForLoginAttempts(userLoginDTO);
        }
        if (user.getIsBlocked()){
            throw new BlockedException(user.getFirstName() + " " + user.getLastName() + " is blocked, contact administrator");
        }
        cacheService.removeUserLoginAttemptsFromCache(user.getId());
        return user;
    }

    private void findCredentialsForLoginAttempts(UserLoginDTO userLoginDTO) {
        if (userRepository.findByEmailAndIsDeletedFalse(userLoginDTO.getEmailOrPhone()) != null){
            loginAttempt(userRepository.findByEmailAndIsDeletedFalse(userLoginDTO.getEmailOrPhone()));
        } else if (userRepository.findByPhoneNumberAndIsDeletedFalse(Long.parseLong(userLoginDTO.getEmailOrPhone())) != null){
            loginAttempt(userRepository.findByPhoneNumberAndIsDeletedFalse(Long.parseLong(userLoginDTO.getEmailOrPhone())));
        } else {
            throw new NotFoundException("Email or phone number is wrong");
        }
        throw new NotFoundException("No user found with provided email or phone number");
    }

    public void loginAttempt(User user) {
        if (cacheService.hasExceededMaxAttempts(user.getId())){
            user.setIsBlocked(true);
            save(user);
            throw new BlockedException(user.getFirstName() + " " + user.getLastName() + " is blocked, contact administrator");
        }
        cacheService.addUserLoginAttempt(user.getId());

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

        validEmailAssertion(email);

        saveEmail(email);
        sendEmailService.sendForgetPasswordEmail(email);
    }

    @Override
    public User getUserToDos(String toDoFolderName, String toDoListName, String userId) {
        User user = userRepository.findByToDoFoldersToDoListsNameAndToDoFoldersNameAndIdAndIsDeletedFalse(toDoListName, toDoFolderName, userId);
        user.getToDoFolders().forEach(element -> element.getToDoLists().removeIf(list -> !list.getName().equals(toDoListName)));
        return user;
    }

    @Override
    public UserDTO getUserDTOById(String userId) {
        User user = new User();
        if (userRepository.findByIdAndIsDeletedFalse(userId).isPresent()) {
            user = userRepository.findByIdAndIsDeletedFalse(userId).get();
        }
        return initializeUserDTO(user);
    }

    private UserDTO initializeUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        if (user.getBirthDay() != null) {
            userDTO.setBirthDay(user.getBirthDay());
            userDTO.setAge(user.getAge());
        }
        if (user.getPhoneNumber() != null) {
            userDTO.setPhoneNumber(user.getPhoneNumber());
        }
        if (user.getProfileImageUrl() != null) {
            userDTO.setProfileImageUrl(user.getProfileImageUrl());
        }
        userDTO.setRole(user.getRole());
        userDTO.setAuthorities(user.getAuthorities());
        userDTO.setIsBlocked(user.getIsBlocked());
        userDTO.setIsDeleted(user.getIsDeleted());
        return userDTO;
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId).get();
    }

    @Override
    public User updateProfileImage(String userId, MultipartFile profileImage) throws IOException {
        User user = userRepository.findByIdAndIsDeletedFalse(userId).get();
        if (profileImage != null) {
            validImageTypeAssertion(profileImage);
            Path userFolder = Paths.get(USER_FOLDER + userId).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + userId + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(userId + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(userId));
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public void deleteProfile(String userId) throws IOException {
        Files.deleteIfExists(Paths.get(USER_FOLDER + userId + FORWARD_SLASH + userId + DOT + JPG_EXTENSION));
        User user = repository.findByIdAndIsDeletedFalse(userId).get();
        user.setProfileImageUrl(null);
        save(user);
    }

    @Override
    public void deleteAccount(String userId) {
        if (userRepository.findByIdAndIsDeletedFalse(userId).isPresent()) {
            User user = userRepository.findByIdAndIsDeletedFalse(userId).get();
            user.setIsDeleted(true);
            save(user);
        } else {
            throw new NotFoundException("No user found with this username");
        }
    }

    @Override
    public Page<User> getAllUsers(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return findAll(pageable);
    }

    @Override
    public UserDTO getUserDTOByIdForUserManagement(String userId) {
        User user = new User();
        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
        }
        return initializeUserDTO(user);
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

    private void validEmailAssertion(String email) {
        if (!isEmailValid(email)) {
            throw new InValidException("The email isn't valid");
        }
    }

    private void validDateAssertion(String date) {
        if (date != null) {
            if (!dateIsValid(date)) {
                throw new InValidException("The birthday provided isn't valid");
            }
        }
    }

    private User initializeUserByUserSignUpDTO(UserSignUpDTO userSignUpDTO) throws Exception {
        User user = new User();
        user.setFirstName(userSignUpDTO.getFirstName());
        user.setPassword(AES.encrypt(userSignUpDTO.getPassword()));
        user.setEmail(userSignUpDTO.getEmail());
        user.setLastName(userSignUpDTO.getLastName());
        user.setValidatorEmail(userSignUpDTO.getEmail());
        user.setRole(Role.ROLE_USER);
        user.setAuthorities(Authority.USER_AUTHORITY);
        return user;
    }

    private void validImageTypeAssertion(MultipartFile profileImage) {
        if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
            throw new IllegalStateException(profileImage.getOriginalFilename() + " is not a suitable file please upload image");
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByValidatorEmailAndIsDeletedFalse(email);
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
