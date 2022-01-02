package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.User;

public interface ListService extends BaseService<User, String> {

    /**
     * Check list name and folder name and username
     * Throw an Empty exception if one or more of the fields are empty, null, whitespace or blank
     * Throw a Doplicate exception if list name if doplicate
     * Throw a Not found exception if no user found with the folder name and username
     * @param folderName name of folder list add to it
     * @param listName name of list add to the folder
     * @param username name of user
     * @return saved user
     */
    User addListToFolder(String folderName, String listName, String username);

    /**
     * Check if there is a to do folder for user that has the same to do list name
     * @param toDoListName name of list
     * @param toDoFolderName name of folder
     * @param username name of user
     * @return true if exists with the list name and folder name
     */
    Boolean existsByToDoListName(String toDoListName,String toDoFolderName, String username);

    /**
     * Check fields
     * Throw an empty exception if one or more fields are empty
     * Throw an empty exception if oldListName is wrong
     * Throw a doplicate exception if newListName is already defined
     * Throw a not found exception if no user found with the folder name provided
     * @param oldListName previous name of list
     * @param newListName new name of list
     * @param folderName name of folder list belongs to
     * @param username username folder belongs to
     */
    void changeListName(String oldListName, String newListName, String folderName, String username);

    /**
     * Check for entries
     * Throw an empty exception if one or more fields are empty, null, blank or whitespace
     * Throw an empty exception if list name or folder name or username is wrong
     * Throw a not found exception if folder name or username is wrong
     * @param listName list name user wants to delete
     * @param folderName folder name list belongs to it
     * @param username username of user folder belongs to it
     */
    void deleteList(String listName, String folderName, String username);
}
