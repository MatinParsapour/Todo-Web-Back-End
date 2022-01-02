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
}
