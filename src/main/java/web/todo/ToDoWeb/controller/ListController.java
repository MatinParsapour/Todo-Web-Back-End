package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.User;
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
     * @param listName name of list
     * @param folderName name of folder
     */
    @PutMapping("/{listName}/add-list-to/{folderName}")
    public User addListToFolder(@PathVariable("listName") String listName, @PathVariable("folderName") String folderName){
        return listService.addListToFolder(folderName,listName, UserSecurity.getUser().getUserName());
    }

    /**
     * Change name of list
     * @param oldListName previous name of the list user wants to change
     * @param newListName new name of list user wants to change to it
     * @param folderName name of folder list belongs to it
     */
    @PutMapping("/{oldListName}/change-to/{newListName}/for-folder/{folderName}")
    public void changeListName(@PathVariable("oldListName") String oldListName, @PathVariable("newListName") String newListName, @PathVariable("folderName") String folderName){
        listService.changeListName(oldListName, newListName, folderName, UserSecurity.getUser().getUserName());
    }

    /**
     * Delete todoList from folder
     * @param listName the list name user wants to delete
     * @param folderName name of folder list belongs to
     */
    @DeleteMapping("/delete-list/{listName}/from/{folderName}")
    public void deleteList(@PathVariable("listName") String listName, @PathVariable("folderName") String folderName){
        listService.deleteList(listName, folderName, UserSecurity.getUser().getUserName());
    }
}
