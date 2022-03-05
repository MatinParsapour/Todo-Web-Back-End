package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.ListDTO;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.service.ListService;
import web.todo.ToDoWeb.util.UserSecurity;

@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    @Autowired
    public ListController(ListService listService) {
        this.listService = listService;
    }


    /**
     * Add a todolist to a folder
     * @param listDTO
     */
    @PutMapping("/add-list-to-folder")
    public User addListToFolder(@RequestBody ListDTO listDTO){
        return listService.addListToFolder(listDTO.getFolderName(),listDTO.getListName(), listDTO.getUsername());
    }

    /**
     * Change name of list
     * @param listDTO
     */
    @PutMapping("/change-list-name")
    public void changeListName(@RequestBody ListDTO listDTO){
        listService.changeListName(listDTO.getOldListName(), listDTO.getNewListName(), listDTO.getFolderName(), listDTO.getUsername());
    }

    /**
     * Delete todoList from folder
     * @param listName the list name user wants to delete
     * @param folderName name of folder list belongs to
     */
    @DeleteMapping("/delete-list/{listName}/{folderName}/{username}")
    public void deleteList(@PathVariable("listName") String listName,
                           @PathVariable("folderName") String folderName,
                           @PathVariable("username") String username){
        listService.deleteList(listName, folderName, username);
    }
}
