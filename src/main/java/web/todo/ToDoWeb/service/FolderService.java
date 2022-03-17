package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

import java.util.Set;

public interface FolderService extends BaseService<User, String> {


    /**
     * Insert a folder to list of user folders
     * @param userid
     * @param folderName name of the folder
     */
    void addFolder(String userid, String folderName);

    /**
     * Check if name of folder is doplicate or not
     * @param folderName the name of folder provided
     * @return true if name of folder already exists
     */
    Boolean existsByToDoFolderName(String folderName, String username);

    /**
     * Check if the username provided belong to a user
     * And then return all of the user to do folders
     * @param userId the username provided
     * @return all to do folders
     */
    Set<ToDoFolder> getUserFolders(String userId);

    /**
     * Check if user has folder with name provided
     * If name is wrong throw an exception
     * @param userId username of user
     * @param toDoFolderName name of folder
     * @return a Set of todoFolders that is folder name is ok returns one todofolder else throw an exception
     */
    Set<ToDoFolder> getToDoFolder(String userId, String toDoFolderName);

    /**
     * Check if old folder name is correct and exists else throw an empty exception
     * Check if new folder name isn't doplicate else throw a doplicate exception
     * Check if username is correct else throw a not found exception
     * @param oldName old folder name user wants to change
     * @param newName new folder name user wants to change folder name to it
     * @param userId username of user to find the folder
     */
    void changeFolderName(String oldName, String newName, String userId);

    /**
     * Check if folder name with username provided exists else throw a not found exception
     * @param folderName the folder name user wants to delete
     * @param userId username of user
     */
    void deleteFolder(String folderName, String userId);
}
