package web.todo.ToDoWeb.service;

import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;

import java.io.IOException;


public interface ToDoService extends BaseService<ToDo, String> {

    /**
     * Get to do and list name and folder name and username
     * Throw an empty exception if to do doesn't have task
     * @param toDo at least include task
     * @param listName name of list to do add to it
     * @param folderName name of folder list belongs to
     * @param userId username of user folder belongs to
     */
    void saveToDoInList(String toDoId, String listName, String folderName, String userId);

    /**
     * Check if to do has task and update to do
     * Throw an empty exception if to do doesn't have task field
     * Throw an empty exception if to do doesn't have id
     * @param toDo the to do user wants to update
     */
    void updateToDo(ToDo toDo);

    /**
     * Delete to do from database and from folder and list
     * @param userId name of user to do belongs to
     */
    void deleteToDo(String userId,String toDoId);

    void deleteToDoPictures(String toDoId);

    /**
     * Save to do in category based on information user saved in to do
     * @param toDo user created
     * @param userId that to do belongs to
     */
    void saveToDoInCategory(ToDo toDo, String userId);

    void addPhoto(String toDoId, MultipartFile picture) throws IOException;

    void deleteToDoPicture(String toDoId, String pictureName) throws IOException;
}
