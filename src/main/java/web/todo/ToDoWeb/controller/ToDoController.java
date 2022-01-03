package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.service.ToDoService;

@RestController
@RequestMapping("/to-do")
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    /**
     * Add to do to the list
     * @param toDo That at least must include task
     * @param listName name of list to do add to it
     * @param folderName name of folder list belongs to
     * @param username username of user folder belongs to
     */
    @PostMapping("/add-to-do/{listName}/folder/{folderName}/for/{username}")
    public void addToDo(@RequestBody ToDo toDo, @PathVariable("listName") String listName, @PathVariable("folderName") String folderName, @PathVariable("username") String username){
        toDoService.saveToDo(toDo, listName, folderName, username);
    }
}
