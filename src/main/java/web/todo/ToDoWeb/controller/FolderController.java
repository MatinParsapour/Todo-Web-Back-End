package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;
import web.todo.ToDoWeb.service.FolderService;
import web.todo.ToDoWeb.service.UserService;

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
     * @param userDTO the user include id
     * @param folderName name of folder user wants to insert must by unique
     */
    @PutMapping("/insert-folder/{folderName}")
    public void insertFolder(@RequestBody UserDTO userDTO, @PathVariable("folderName") String folderName){
        folderService.addFolder(userDTO,folderName);
    }

    /**
     * Return all of user to do folders
     * @param userDTO the username
     * @return to do folders for user
     */
    @GetMapping("/get-todo-folders")
    public Set<ToDoFolder> getToDoFolders(@RequestBody UserDTO userDTO){
        return folderService.getUserFolders(userDTO.getUserName());
    }

    /**
     * Get todoFolder that user looking for
     * @param userSignUpDTO the username of user
     * @param toDoFolderName the name of folder user looking for
     * @return a Set of todofolders
     */
    @GetMapping("/get-todo-folder/{toDoFolderName}")
    public Set<ToDoFolder> getToDoFolder(@RequestBody UserSignUpDTO userSignUpDTO, @PathVariable("toDoFolderName") String toDoFolderName){
        return folderService.getToDoFolder(userSignUpDTO.getUserName(),toDoFolderName);
    }

    @PutMapping("/{oldName}/change-to/{newName}")
    public void changeFolderName(@PathVariable("oldName") String oldName, @PathVariable("newName") String newName, @RequestBody UserDTO userDTO){
        folderService.changeFolderName(oldName,newName,userDTO.getUserName());
    }
}
