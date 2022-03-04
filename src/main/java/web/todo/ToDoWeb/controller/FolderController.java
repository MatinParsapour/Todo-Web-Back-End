package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.dto.FolderDTO;
import web.todo.ToDoWeb.model.dto.UpdateFolderDTO;
import web.todo.ToDoWeb.service.FolderService;
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
     * @param folderDTO the object with username and folder name
     */
    @PutMapping("/insert-folder")
    public void insertFolder(@RequestBody FolderDTO folderDTO){
        folderService.addFolder(folderDTO.getUsername(), folderDTO.getFolderName());
    }

    @GetMapping("/exists-by-folder-name/{folderName}/{username}")
    public Boolean existsByFolderName(@PathVariable("folderName") String folderName, @PathVariable("username") String username){
        return folderService.existsByToDoFolderName(folderName, username);
    }

    /**
     * Return all of user to do folders
     * @return to do folders for user
     */
    @GetMapping("/get-todo-folders/{username}")
    public Set<ToDoFolder> getToDoFolders(@PathVariable("username") String username){
        return folderService.getUserFolders(username);
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
     * *param updateFolderDTO
     */
    @PutMapping("/change-folder-name")
    public void changeFolderName(@RequestBody UpdateFolderDTO updateFolderDTO){
        folderService.changeFolderName(updateFolderDTO.getOldName(),updateFolderDTO.getNewName(),updateFolderDTO.getUsername());
    }

    /**
     * Get folder name and user and send to service to delete folder by folder name
     * @param username
     * @param folderName
     */
    @DeleteMapping("/delete-folder/{username}/{folderName}")
    public void deleteFolder(@PathVariable("username") String username, @PathVariable("folderName") String folderName){
        folderService.deleteFolder(folderName,username);
    }
}
