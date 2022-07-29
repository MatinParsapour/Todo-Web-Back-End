package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

public interface ListService extends BaseService<User, String> {

    void addListToFolder(String folderName, String listName, String username);

    Boolean existsByToDoListName(String toDoListName,String toDoFolderName, String userId);

    void changeListName(String oldListName, String newListName, String folderName, String userId);

    void deleteList(String listName, String folderName, String userId);

    void insertToDoToList(ToDo toDo, String listName, String folderName, String userId);

    void removeToDoFromList(String username, String toDoId);

    void notEmptyAssertion(String attribute);
}
