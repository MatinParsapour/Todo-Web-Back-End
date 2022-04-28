package web.todo.ToDoWeb.service;

import org.springframework.web.multipart.MultipartFile;
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

    void saveToDoInCategory(ToDo toDo, String userId);

    void addPhoto(String toDoId, MultipartFile picture) throws IOException;

    void deleteToDoPicture(String toDoId, String pictureName) throws IOException;

    Set<ToDo> getStarredToDos(String userId);

    List<ToDo> getAllToDos();

    void like(String userId, String todoId);
}
