package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.service.FolderService;
import web.todo.ToDoWeb.service.UserService;
import web.todo.ToDoWeb.util.UserSecurity;

import java.util.Set;

@RestController
@RequestMapping("/folder")
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }


    /**
     * Add a folder to list of user folders
     * @param folderName name of folder user wants to insert must by unique
     */
    @PutMapping("/insert-folder/{folderName}")
    public void insertFolder(@PathVariable("folderName") String folderName){
        folderService.addFolder(UserSecurity.getUser(),folderName);
    }

    /**
     * Return all of user to do folders
     * @return to do folders for user
     */
    @GetMapping("/get-todo-folders")
    public Set<ToDoFolder> getToDoFolders(){
        return folderService.getUserFolders(UserSecurity.getUser().getUserName());
    }

    /**
     * Get todoFolder that user looking for
     * @param toDoFolderName the name of folder user looking for
     * @return a Set of todofolders
     */
    @GetMapping("/get-todo-folder/{toDoFolderName}")
    public Set<ToDoFolder> getToDoFolder(@PathVariable("toDoFolderName") String toDoFolderName){
        return folderService.getToDoFolder(UserSecurity.getUser().getUserName(),toDoFolderName);
    }

    /**
     * Change name of folder
     * @param oldName name of the old folder
     * @param newName new name user wants to change folder to
     */
    @PutMapping("/{oldName}/change-to/{newName}")
    public void changeFolderName(@PathVariable("oldName") String oldName, @PathVariable("newName") String newName){
        folderService.changeFolderName(oldName,newName,UserSecurity.getUser().getUserName());
    }

    /**
     * Get folder name and user and send to service to delete folder by folder name
     * @param folderName name of folder user wants to delete
     */
    @DeleteMapping("/delete-folder/{folderName}")
    public void deleteFolder(@PathVariable("folderName") String folderName){
        folderService.deleteFolder(folderName, UserSecurity.getUser().getUserName());
    }
}
