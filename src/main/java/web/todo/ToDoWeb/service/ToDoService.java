package web.todo.ToDoWeb.service;

import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.enumeration.AccessLevel;
import web.todo.ToDoWeb.model.Comment;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface ToDoService extends BaseService<ToDo, String> {

    void saveToDoInList(String toDoId, String listName, String folderName, String userId);

    void updateToDo(ToDo toDo);

    void deleteToDo(String userId,String toDoId);

    void deleteToDoPictures(String toDoId);

    void saveTodo(ToDo toDo, String userId);

    void addPhoto(String toDoId, MultipartFile picture) throws IOException;

    void deleteToDoPicture(String toDoId, String pictureName) throws IOException;

    Set<ToDo> getStarredToDos(String userId);

    List<ToDo> getAllToDos();

    void updateUserToDosAccessLevel(String username, AccessLevel accessLevel);

    Set<ToDo> getUserToDos(String username);

    void like(String userId, String todoId);

    void disLike(String userId, String todoId);

    ToDo getToDoById(String toDoId);

    void addCommentToComments(Comment newComment, String todoId);

    void deleteCommentFromToDoComments(String commentId, String todoId);

    void addToDoToUserToDos(String todoId, String userId);

    void saveToDoForUser(String todoId, String userId);

    void findTags(ToDo todo, User user);

    void tagProgress(ToDo todo, String section, User user);

    ToDo getByComment(Comment comment);
}
