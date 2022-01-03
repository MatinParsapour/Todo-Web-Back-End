package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.dto.UserDTO;

public interface ToDoService extends BaseService<ToDo, String> {

    /**
     * Get to do and list name and folder name and username
     * Throw an empty exception if to do doesn't have task
     * @param toDo at least include task
     * @param listName name of list to do add to it
     * @param folderName name of folder list belongs to
     * @param username username of user folder belongs to
     */
    void addToDo(ToDo toDo, String listName, String folderName, String username);
}
