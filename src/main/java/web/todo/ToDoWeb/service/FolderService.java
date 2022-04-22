package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

import java.util.Set;

public interface FolderService extends BaseService<User, String> {

    void addFolder(String userid, String folderName);

    Boolean existsByToDoFolderName(String folderName, String username);

    Set<ToDoFolder> getUserFolders(String userId);

    Set<ToDoFolder> getToDoFolder(String userId, String toDoFolderName);

    void changeFolderName(String oldName, String newName, String userId);

    void deleteFolder(String folderName, String userId);
}
