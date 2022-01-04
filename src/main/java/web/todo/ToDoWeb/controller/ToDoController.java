package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.service.ToDoService;
import web.todo.ToDoWeb.service.UserService;

@RestController
@RequestMapping("/to-do")
public class ToDoController {

    private final ToDoService toDoService;
    private final UserService userService;

    @Autowired
    public ToDoController(ToDoService toDoService, UserService userService) {
        this.toDoService = toDoService;
        this.userService = userService;
    }

    /**
     * Add to do to the list
     * @param toDo That at least must include task
     * @param listName name of list to do add to it
     * @param folderName name of folder list belongs to
     * @param username username of user folder belongs to
     */
    @PostMapping("/add-to-do/{listName}/folder/{folderName}/for/{username}")
    public void addToDoToList(@RequestBody ToDo toDo, @PathVariable("listName") String listName, @PathVariable("folderName") String folderName, @PathVariable("username") String username){
        toDoService.saveToDoInList(toDo, listName, folderName, username);
    }

    /**
     * Add to do to the specific category
     * @param toDo that user created at least have task
     * @param userId id of user to do belongs to
     */
    @PostMapping("/add-to-do/{userId}")
    public void addToDoToCategory(@RequestBody ToDo toDo, @PathVariable("userId") String userId){
        User user = userService.findById(userId).get();
        toDoService.saveToDoInCategory(toDo, user);
    }

    /**
     * Update to do
     * @param toDo that at least include task
     */
    @PutMapping("/update-to-do")
    public void updateToDo(@RequestBody ToDo toDo){
        toDoService.updateToDo(toDo);
    }

    @DeleteMapping("/delete-to-do/{folderName}/{listName}/{userName}")
    public void getToDos(@PathVariable("folderName") String folderName, @PathVariable("listName") String listName, @PathVariable("userName") String userName, @RequestBody ToDo toDo){
        toDoService.deleteToDo(folderName, listName, userName, toDo);
    }
}
