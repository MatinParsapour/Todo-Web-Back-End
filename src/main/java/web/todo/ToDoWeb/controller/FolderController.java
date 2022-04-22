package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.dto.FolderDTO;
import web.todo.ToDoWeb.model.dto.UpdateFolderDTO;
import web.todo.ToDoWeb.service.FolderService;

import java.util.Set;

@RestController
@RequestMapping("/folder")
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PutMapping("/insert-folder")
    public void insertFolder(@RequestBody FolderDTO folderDTO){
        folderService.addFolder(folderDTO.getUserId(), folderDTO.getFolderName());
    }

    @GetMapping("/exists-by-folder-name/{folderName}/{userId}")
    public Boolean existsByFolderName(@PathVariable("folderName") String folderName, @PathVariable("userId") String userId){
        return folderService.existsByToDoFolderName(folderName, userId);
    }

    @GetMapping("/get-todo-folders/{userId}")
    public Set<ToDoFolder> getToDoFolders(@PathVariable("userId") String userId){
        return folderService.getUserFolders(userId);
    }

    @GetMapping("/get-todo-folder/{toDoFolderName}/{userId}")
    public Set<ToDoFolder> getToDoFolder(@PathVariable("toDoFolderName") String toDoFolderName, @PathVariable("userId") String userId){
        return folderService.getToDoFolder(userId,toDoFolderName);
    }

    @PutMapping("/change-folder-name")
    public void changeFolderName(@RequestBody UpdateFolderDTO updateFolderDTO){
        folderService.changeFolderName(updateFolderDTO.getOldName(),updateFolderDTO.getNewName(),updateFolderDTO.getUserId());
    }

    @DeleteMapping("/delete-folder/{userId}/{folderName}")
    public void deleteFolder(@PathVariable("userId") String userId, @PathVariable("folderName") String folderName){
        folderService.deleteFolder(folderName,userId);
    }
}
