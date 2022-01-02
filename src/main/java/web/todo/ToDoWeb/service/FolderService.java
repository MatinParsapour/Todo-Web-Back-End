package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

import java.util.Set;

public interface FolderService extends BaseService<User, String> {


    /**
     * Insert a folder to list of user folders
     * @param userDTO the user include id
     * @param folderName name of the folder
     */
    void addFolder(UserDTO userDTO, String folderName);

    /**
     * Check if name of folder is doplicate or not
     * @param folderName the name of folder provided
     * @return true if name of folder already exists
     */
    Boolean existsByToDoFolderName(String folderName, String username);

    /**
     * Check if the username provided belong to a user
     * And then return all of the user to do folders
     * @param username the username provided
     * @return all to do folders
     */
    Set<ToDoFolder> getUserFolders(String username);

    /**
     * Check if user has folder with name provided
     * If name is wrong throw an exception
     * @param username username of user
     * @param toDoFolderName name of folder
     * @return a Set of todoFolders that is folder name is ok returns one todofolder else throw an exception
     */
    Set<ToDoFolder> getToDoFolder(String username, String toDoFolderName);
}
