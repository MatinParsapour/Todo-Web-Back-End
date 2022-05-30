package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Comment;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.ToDoRepository;
import web.todo.ToDoWeb.service.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.*;
import static web.todo.ToDoWeb.constants.FileConstants.*;

@Service
public class ToDoServiceImpl extends BaseServiceImpl<ToDo, String, ToDoRepository> implements ToDoService, FilledValidation {

    private final ToDoRepository toDoRepository;
    private final UserService userService;
    private final ListService listService;

    @Autowired
    public ToDoServiceImpl(ToDoRepository repository, ToDoRepository toDoRepository, UserService userService, ListService listService) {
        super(repository);
        this.toDoRepository = toDoRepository;
        this.userService = userService;
        this.listService = listService;
    }

    @Override
    public void saveToDoInList(String toDoId, String listName, String folderName, String userId) {
        ToDo todo = findById(toDoId)
                .orElseThrow(() ->  new NotFoundException("No todo found"));
        listService.insertToDoToList(todo, listName, folderName, userId);
    }

    @Override
    public void updateToDo(ToDo toDo) {
        if (isFilled(toDo.getTask())) {
            throw new EmptyException("For to do at least you should fill task");
        }
        if (isFilled(toDo.getId())) {
            throw new EmptyException("The to do must have id");
        }
        save(toDo);
    }

    @Override
    public void deleteToDo(String userId, String toDoId) {
        if (isFilled(toDoId)) {
            throw new EmptyException("The id field is empty");
        }
        userService.removeFromToDos(userId, toDoId);
        listService.removeToDoFromList(userId, toDoId);
        deleteToDoPictures(toDoId);
        deleteById(toDoId);
    }

    private boolean isFilled(String property) {
        return !isEmpty(property) || !isBlank(property) || !isNull(property) || !isWhiteSpace(property);
    }

    @Override
    public void deleteToDoPictures(String toDoId) {
        File file = new File(TODO_FOLDER + toDoId);
        File[] files = file.listFiles();
        if (files != null){
            for (File subFile : files) {
                subFile.delete();
            }
            file.delete();
        }
    }

    @Override
    public void saveToDoInCategory(ToDo toDo, String userId) {
        if (isFilled(toDo.getTask())) {
            throw new EmptyException("For to do at least you should fill task");
        }
        ToDo savedToDo = save(toDo);
        userService.addToDoToUserToDos(savedToDo, userId);
    }

    @Override
    public void addPhoto(String toDoId, MultipartFile picture) throws IOException {
        ToDo toDo = findById(toDoId).orElseThrow(() -> new NotFoundException("No todo found with provided id"));
        if (picture != null) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(picture.getContentType())) {
                throw new IllegalStateException("Type of file is invalid");
            }
            Path toDoFolder = Paths.get(TODO_FOLDER + toDoId).toAbsolutePath().normalize();
            if (!Files.exists(toDoFolder)) {
                Files.createDirectories(toDoFolder);
            }
            String fileName = validateFileName(picture, toDoFolder);
            toDo.getPictures().add(setPictureImageUrl(toDoId, fileName));
            save(toDo);
        }
    }

    private String validateFileName(MultipartFile picture, Path requestFolder) throws IOException {
        String fileName = picture.getOriginalFilename();
        try {
            Files.copy(picture.getInputStream(), requestFolder.resolve(fileName));
        } catch (FileAlreadyExistsException exception){
            fileName = UUID.randomUUID() + DOT + JPG_EXTENSION;
            Files.copy(picture.getInputStream(), requestFolder.resolve(fileName));
        }
        return fileName;
    }


    @Override
    public void deleteToDoPicture(String toDoId, String pictureName) throws IOException {
        Path pictureDirectory = Paths.get(TODO_FOLDER + toDoId + FORWARD_SLASH + pictureName).toAbsolutePath().normalize();
        Files.deleteIfExists(pictureDirectory);
        ToDo toDo = findById(toDoId).get();
        toDo.getPictures().removeIf(element -> element.equals(ServletUriComponentsBuilder.fromCurrentContextPath().path(TODO_IMAGE_PATH + toDoId + FORWARD_SLASH + pictureName).toUriString()));
        save(toDo);
    }

    @Override
    public Set<ToDo> getStarredToDos(String userId) {
        Set<ToDo> toDos = new HashSet<>();
        User user = userService.findById(userId).get();
        user.getToDos().forEach(toDo -> {
            if (toDo.getIsStarred()){
                toDos.add(toDo);
            }
        });
        return toDos;
    }

    @Override
    public List<ToDo> getAllToDos() {
        List<ToDo> todos = toDoRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        todos.forEach(todo -> {
            todo.getComments().forEach(comment -> nullImportantProperties(comment.getUser()));
            todo.getLikes().forEach(this::nullImportantProperties);
        });
        return todos;
    }

    @Override
    public void like(String userId, String todoId) {
        if (findById(todoId).isEmpty() && userService.findById(userId).isEmpty()){
            return;
        }

        addUserToToDoLikes(userId, todoId);
    }

    @Override
    public void disLike(String userId, String todoId) {
        if (findById(todoId).isEmpty() && userService.findById(userId).isEmpty()){
            return;
        }

        removeUserFromToDoLikes(userId, todoId);
    }

    @Override
    public ToDo getToDoById(String toDoId) {
        ToDo toDo = findById(toDoId).get();
        toDo.getLikes().forEach(this::nullImportantProperties);
        toDo.getComments().forEach(comment -> nullImportantProperties(comment.getUser()));
        return toDo;
    }

    @Override
    public void addCommentToComments(Comment newComment, String todoId) {
        if (findById(todoId).isEmpty()){
            return;
        }

        saveCommentInToDoComments(newComment, todoId);
    }

    @Override
    public void deleteCommentFromToDoComments(String commentId, String todoId) {
        if (findById(todoId).isEmpty()){
            return;
        }

        ToDo toDo = findById(todoId).get();
        toDo.getComments().removeIf(comment -> comment.getId().equals(commentId));
        save(toDo);
    }

    @Override
    public void addToDoToUserToDos(String todoId, String userId) {
        if (findById(todoId).isEmpty()){
            return;
        }

        ToDo toDo = findById(todoId).get();
        ToDo newToDo = createToDo(toDo);
        ToDo savedToDo = save(newToDo);
        userService.addToDoToUserToDos(savedToDo, userId);
    }

    @Override
    public void saveToDoForUser(String todoId, String userId) {
        if (findById(todoId).isEmpty()){
            return;
        }

        ToDo toDo = findById(todoId).get();
        userService.addToSaved(toDo, userId);
    }

    private ToDo createToDo(ToDo toDo) {
        ToDo newToDo = new ToDo();
        newToDo.setCategory(toDo.getCategory());
        newToDo.setCreatedAt(new Date());
        newToDo.setDateTime(toDo.getDateTime());
        newToDo.setNote(toDo.getNote());
        newToDo.setTask(toDo.getTask());
        return newToDo;
    }

    private void saveCommentInToDoComments(Comment newComment, String todoId) {
        ToDo toDo = findById(todoId).get();
        toDo.getComments().add(newComment);
        save(toDo);
    }

    private void removeUserFromToDoLikes(String userId, String todoId) {
        ToDo toDo = findById(todoId).get();
        User user = userService.findById(userId).get();
        toDo.getLikes().removeIf(like -> like.getId().equals(user.getId()));
        save(toDo);
    }

    private void addUserToToDoLikes(String userId, String todoId) {
        ToDo toDo = findById(todoId).get();
        User user = userService.findById(userId).get();
        toDo.getLikes().add(user);
        save(toDo);
    }

    private String setPictureImageUrl(String toDoId, String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(TODO_IMAGE_PATH + toDoId + "/" + fileName).toUriString();
    }

    private void nullImportantProperties(User user) {
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
}

