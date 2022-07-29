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
import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserLoginDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.repository.ToDoRepository;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;
import static web.todo.ToDoWeb.constants.Authority.*;
import static web.todo.ToDoWeb.constants.FileConstants.*;

@Service

public class UserServiceImpl extends BaseServiceImpl<User, String, UserRepository>
        implements UserService, UniqueValidation, FilledValidation, EmailValidation {

    private final UserRepository userRepository;
    private final ToDoRepository toDoRepository;
    private final SendEmailService sendEmailService;
    private final CacheService cacheService;
    private final PhoneService phoneService;
    private final TagService tagService;
    private UserSignUpDTO userSignUpDTO;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserRepository userRepository, ToDoRepository toDoRepository, SendEmailService sendEmailService, CacheService cacheService, PhoneService phoneService, TagService tagService) {
        super(repository);
        this.userRepository = userRepository;
        this.toDoRepository = toDoRepository;
        this.sendEmailService = sendEmailService;
        this.cacheService = cacheService;
        this.phoneService = phoneService;
        this.tagService = tagService;
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
        if (!userRepository.existsByEmailAndIsDeletedFalse(userSignUpDTO.getEmail())) {
            User user = initializeUserByUserSignUpDTO(userSignUpDTO);
            User updateUserLastLogin = updateUserLastLogin(user);
            save(updateUserLastLogin);
            return initializeUserDTO(updateUserLastLogin);
        } else if (userRepository.findByEmailAndIsDeletedFalseAndIsBlockedFalse(userSignUpDTO.getEmail()) == null) {
            throw new BlockedException("Your account is blocked");
        }
        User user = userRepository.findByEmailAndIsDeletedFalseAndIsBlockedFalse(userSignUpDTO.getEmail());
        User updatedUser = updateUserLastLogin(user);
        return initializeUserDTO(updatedUser);
    }

    @Override
    public UserDTO updateDTO(UserDTO userDTO) {
        User user = findById(userDTO.getId()).orElseThrow(() -> new NotFoundException("No user found with provided id"));

        validDateAssertion(userDTO.getBirthDay());

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setBirthDay(userDTO.getBirthDay());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setIsBlocked(userDTO.getIsBlocked());
        user.setAccessLevel(userDTO.getAccessLevel());
        changeRole(user, userDTO);
        user.setIsDeleted(userDTO.getIsDeleted());
        updateToDosAccessLevel(user, userDTO);
        save(user);
        return initializeUserDTO(user);
    }

    private void updateToDosAccessLevel(User user, UserDTO userDTO) {
        Set<ToDo> toDos = user.getToDos().stream().peek(todo -> {
            todo.setAccessLevel(userDTO.getAccessLevel());
            toDoRepository.save(todo);
        }).collect(Collectors.toSet());
        user.setToDos(toDos);
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
        User user = findById(userDTO.getId()).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        user.setIsDeleted(true);
        save(user);
    }

    private User updateUserLastLogin(User user) {
        user.setLastLoginDate(new Date());
        return save(user);
    }

    @Override
    public UserDTO logInUser(UserLoginDTO userLoginDTO) throws Exception {
        boolean isUserExists = userRepository.findByUserNameAndPasswordAndIsDeletedFalse(userLoginDTO.getUserName(), AES.encrypt(userLoginDTO.getPassword())).isPresent();

        if (!isUserExists) {
            validateIfUserNotBlocked(userLoginDTO);
            return null;
        }

        User user = userRepository.findByUserNameAndPasswordAndIsDeletedFalse(userLoginDTO.getUserName(), AES.encrypt(userLoginDTO.getPassword())).get();
        cacheService.removeUserLoginAttemptsFromCache(user.getId());
        User updatedUser = updateUserLastLogin(user);
        return initializeUserDTO(updatedUser);
    }

    private void validateIfUserNotBlocked(UserLoginDTO userLoginDTO) {
        boolean isUserExists = userRepository.findByUserNameAndPasswordAndIsDeletedFalse(userLoginDTO.getUserName(), userLoginDTO.getPassword()).isPresent();

        if (!isUserExists)
            throw new NotFoundException("No user found");

        User user = userRepository.findByUserNameAndPasswordAndIsDeletedFalse(userLoginDTO.getUserName(), userLoginDTO.getPassword()).get();

        if (user.getIsBlocked())
            throw new BlockedException(user.getFirstName() + " " + user.getLastName() + " is blocked, contact administrator");

        loginAttempt(user);
    }

    public void loginAttempt(User user) {
        if (!cacheService.hasExceededMaxAttempts(user.getId())) {
            cacheService.addUserLoginAttempt(user.getId());
            return;
        }

        user.setIsBlocked(true);
        save(user);
        throw new BlockedException(user.getFirstName() + " " + user.getLastName() + " is blocked, contact administrator");

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

        User user = userRepository.findByEmailAndIsDeletedFalse(email).get();
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
    public UserDTO getUserDTOByUsername(String userId) {
        User user = userRepository.findByUserNameAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        return initializeUserDTO(user);
    }

    private UserDTO initializeUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserName(user.getUserName());
        userDTO.setPhoneVisible(user.isPhoneVisible());
        userDTO.setEmailVisible(user.isEmailVisible());
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
        userDTO.setRegisterDate(user.getRegisterDate());
        userDTO.setLastLoginDate(user.getLastLoginDate());
        Set<User> followers = user.getFollowers().stream().peek(this::removeUserCrucialInfo).collect(Collectors.toSet());
        Set<User> followings = user.getFollowings().stream().peek(this::removeUserCrucialInfo).collect(Collectors.toSet());
        userDTO.setFollowers(followers);
        userDTO.setFollowings(followings);
        userDTO.setAccessLevel(user.getAccessLevel());
        userDTO.setBio(user.getBio());
        return userDTO;
    }

    private void removeUserCrucialInfo(User user) {
        user.setFollowings(null);
        user.setFollowers(null);
        user.setToDos(null);
        user.setPassword(null);
        user.setPhoneNumber(null);
        user.setAge(0);
        user.setBirthDay(null);
        user.setIsBlocked(null);
        user.setRegisterDate(null);
        user.setRole(null);
        user.setToDoFolders(null);
        user.setLastLoginDate(null);
        user.setAuthorities(null);
        user.setIsDeleted(null);
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findByUserNameAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
    }

    @Override
    public User updateProfileImage(String userId, MultipartFile profileImage) throws IOException {
        User user = userRepository.findByUserNameAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
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
        User user = repository.findByUserNameAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        user.setProfileImageUrl(null);
        save(user);
    }

    @Override
    public void deleteAccount(String userId) {
        User user = userRepository.findByUserNameAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        user.setIsDeleted(true);
        save(user);
    }

    @Override
    public Page<User> getAllUsers(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> all = findAll(pageable);
        all.getContent().forEach(user -> {
            user.setFollowers(null);
            user.setFollowings(null);
        });
        return all;
    }

    @Override
    public UserDTO getUserDTOByIdForUserManagement(String userId) {
        User user = findById(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        return initializeUserDTO(user);
    }

    @Override
    public void addToDoToUserToDos(ToDo todo, String userId) {
        User user = findByUsername(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        user.getToDos().add(todo);
        save(user);
    }

    @Override
    public void removeFromToDos(String userId, String toDoId) {
        User user = findById(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        user.getToDos().removeIf(toDo -> toDo.getId().equals(toDoId));
        save(user);
    }

    @Override
    public void addToFollowers(User responder, User applicant) {
        responder.getFollowers().add(applicant);
        save(responder);
    }

    @Override
    public void addToFollowings(User responder, User applicant) {
        applicant.getFollowings().add(responder);
        save(applicant);
    }

    @Override
    public void unFollowUser(String username, String followingUsername) {
        User user = findByUsername(username).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        user.getFollowings().removeIf(person -> person.getUserName().equals(followingUsername));
        save(user);
    }

    @Override
    public User findUserByToDoId(String todoId) {
        User userFoundByToDoId = userRepository.findByToDos(toDoRepository.findById(todoId).get());
        userFoundByToDoId.setFollowings(null);
        userFoundByToDoId.setFollowers(null);
        return userFoundByToDoId;
    }

    @Override
    public void addToSaved(ToDo toDo, String userId) {
        User user = userRepository.findByUserNameAndIsDeletedFalse(userId).orElseThrow(() -> new NotFoundException("No user found with provided id"));
        for (ToDo userToDo: user.getSavedToDos()){
            if (toDo.getId().equals(userToDo.getId())){
                return;
            }
        }
        user.getSavedToDos().add(toDo);
        save(user);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUserNameAndIsDeletedFalse(username);
    }

    @Override
    public void forgetUsername(String emailOrPhoneNumber) throws MessagingException, UnsupportedEncodingException {
        boolean isUserEnteredEmail = isEmailValid(emailOrPhoneNumber);
        if (isUserEnteredEmail) {
            if (!existsByEmail(emailOrPhoneNumber)) {
                throw new NotFoundException("No user found by " + emailOrPhoneNumber);
            }
            sendEmailService.sendForgetUsernameEmail(emailOrPhoneNumber);
            return;
        }
        if (!existsByPhoneNumber(Long.parseLong(emailOrPhoneNumber))) {
            throw new NotFoundException("No user found by " + emailOrPhoneNumber);
        }
        phoneService.validatePhoneNumberAndUsername(Long.parseLong(emailOrPhoneNumber), userRepository.findByPhoneNumberAndIsDeletedFalse(Long.parseLong(emailOrPhoneNumber)).orElseThrow(() -> new NotFoundException("No user found with " + emailOrPhoneNumber)).getId());
    }

    @Override
    public void checkForgetUsernameCode(String code, String emailOrPhoneNumber) {
        boolean isUserEnteredEmail = isEmailValid(emailOrPhoneNumber);
        if (isUserEnteredEmail) {
            validateEmailAndCode(emailOrPhoneNumber, code);
        } else {
            Boolean isCodeValid = phoneService.isCodeValid(Integer.parseInt(code));
            if (!isCodeValid) {
                throw new InValidException("The code is invalid");
            }
        }
    }

    @Override
    public void changeUsername(String newUsername, String emailOrPhoneNumber) {
        if (existsByUsername(newUsername)) {
            throw new DoplicateException(newUsername + " already exists, select another username");
        }

        boolean isUserEnteredEmail = isEmailValid(emailOrPhoneNumber);
        User user;
        if (isUserEnteredEmail) {
            user = userRepository.findByEmailAndIsDeletedFalse(emailOrPhoneNumber).orElseThrow(() -> new NotFoundException("No user found with " + emailOrPhoneNumber));
        } else {
            user = userRepository.findByPhoneNumberAndIsDeletedFalse(Long.parseLong(emailOrPhoneNumber)).orElseThrow(() -> new NotFoundException("No user found with " + emailOrPhoneNumber));
        }
        user.setUserName(newUsername);
        save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserNameAndIsDeletedFalse(username);
    }

    @Override
    public List<User> search(String keyword) {
        return repository.findByUserNameContainsOrBioContains(keyword, keyword);
    }

    @Override
    public void followTag(String username, String tagName) {
        User user = findByUsername(username).orElseThrow(() -> new NotFoundException("No user found by " + username));
        Tag tag = tagService.getByName(tagName);
        user.getTags().add(tag);
        save(user);
    }

    @Override
    public boolean isTagFollowed(String username, String tagName) {
        return repository.existsByTagsAndUserName(tagService.getByName(tagName), username);
    }

    @Override
    public void unFollowTag(String username, String tagName) {
        User user = findByUsername(username).orElseThrow(() -> new NotFoundException("No user found by " + username));
        Tag tag = tagService.getByName(tagName);
        user.getTags().removeIf(element -> element.getId().equals(tag.getId()));
        save(user);
    }

    @Override
    public List<User> getFollowers(String username) {
        User user = findByUsername(username).orElseThrow(() -> new NotFoundException("No user found by " + username));
        return user.getFollowers().stream().peek(this::removeUserCrucialInfo).collect(Collectors.toList());
    }

    @Override
    public List<User> getFollowings(String username) {
        User user = findByUsername(username).orElseThrow(() -> new NotFoundException("No user found by " + username));
        return user.getFollowings().stream().peek(this::removeUserCrucialInfo).collect(Collectors.toList());
    }

    @Override
    public List<Tag> getTags(String username) {
        List<Tag> tags = findByUsername(username).orElseThrow(() -> new NotFoundException("No user found by " + username)).getTags();
        tags = tags.stream().peek(tag -> {
            tag.setCreatedBy(null);
            tag.setToDos(null);
        }).collect(Collectors.toList());
        return tags;
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
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);
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
        if (userSignUpDTO.getProvider() == null) {
            throw new InValidException("You have to provide Provider");
        }
        User user = new User();
        user.setProvider(userSignUpDTO.getProvider());
        user.setFirstName(userSignUpDTO.getFirstName());
        user.setLastName(userSignUpDTO.getLastName());
        user.setUserName(userSignUpDTO.getUserName());
        user.setPassword(AES.encrypt(userSignUpDTO.getPassword()));
        user.setEmail(userSignUpDTO.getEmail());
        user.setRole(Role.ROLE_USER);
        user.setAuthorities(Authority.USER_AUTHORITY);
        user.setRegisterDate(new Date());
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
    public Boolean existsByPhoneNumber(Long phoneNumber) {
        return userRepository.existsByPhoneNumberAndIsDeletedFalse(phoneNumber);
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
