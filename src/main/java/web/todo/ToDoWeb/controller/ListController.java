package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.service.ListService;

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
     * @param listName name of list
     * @param folderName name of folder
     * @param userDTO user dto include username
     */
    @PutMapping("/{listName}/add-list-to/{folderName}")
    public User addListToFolder(@PathVariable("listName") String listName, @PathVariable("folderName") String folderName, @RequestBody UserDTO userDTO){
        return listService.addListToFolder(folderName,listName,userDTO.getUserName());
    }

    /**
     * Change name of list
     * @param oldListName previous name of the list user wants to change
     * @param newListName new name of list user wants to change to it
     * @param folderName name of folder list belongs to it
     * @param userDTO include username
     */
    @PutMapping("/{oldListName}/change-to/{newListName}/for-folder/{folderName}")
    public void changeListName(@PathVariable("oldListName") String oldListName, @PathVariable("newListName") String newListName, @PathVariable("folderName") String folderName, @RequestBody UserDTO userDTO){
        listService.changeListName(oldListName, newListName, folderName, userDTO.getUserName());
    }
}
