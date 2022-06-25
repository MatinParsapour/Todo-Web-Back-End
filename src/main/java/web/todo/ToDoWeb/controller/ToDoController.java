package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.service.ToDoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static web.todo.ToDoWeb.constants.FileConstants.FORWARD_SLASH;
import static web.todo.ToDoWeb.constants.FileConstants.TODO_FOLDER;

@RestController
@RequestMapping("/to-do")
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PutMapping("/add-to-do/{todoId}/list/{listName}/folder/{folderName}/for/{userId}")
    public void addToDoToList(@PathVariable("todoId") String toDoId, @PathVariable("listName") String listName, @PathVariable("folderName") String folderName, @PathVariable("userId") String userId){
        toDoService.saveToDoInList(toDoId, listName, folderName, userId);
    }

    @PostMapping("/add-to-do/{userId}")
    public void addToDoToCategory(@RequestBody ToDo toDo, @PathVariable("userId") String userId){
        toDoService.saveTodo(toDo, userId);
    }

    @PutMapping("/update-to-do")
    public void updateToDo(@RequestBody ToDo toDo){
        toDoService.updateToDo(toDo);
    }

    @DeleteMapping("/delete-to-do/{userId}/{toDoId}")
    public void deleteToDo(@PathVariable("userId") String userId, @PathVariable("toDoId") String toDoId){
        toDoService.deleteToDo(userId, toDoId);
    }

    @PutMapping("/add-photo")
    public void addPhoto(@RequestParam("toDoId") String toDoId, @RequestParam("picture")MultipartFile picture) throws IOException {
        toDoService.addPhoto(toDoId, picture);
    }

    @GetMapping(value = "/image/{toDoId}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getToDoImage(@PathVariable("toDoId") String toDoId, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(TODO_FOLDER + toDoId + FORWARD_SLASH + fileName));
    }

    @GetMapping("/get-to-do/{toDoId}")
    public ToDo getToDo(@PathVariable("toDoId") String toDoId){
        return toDoService.getToDoById(toDoId);
    }

    @DeleteMapping("/delete-photo/{toDoId}/{pictureName}")
    public void deleteToDoPicture(@PathVariable("toDoId") String toDoId, @PathVariable("pictureName") String pictureName) throws IOException {
        toDoService.deleteToDoPicture(toDoId, pictureName);
    }

    @GetMapping("/get-starred-todos/{userId}")
    public Set<ToDo> getStarredToDos(@PathVariable("userId") String userId){
        return toDoService.getStarredToDos(userId);
    }

    @GetMapping("/explore")
    public List<ToDo> explore(){
        return toDoService.getAllToDos();
    }

    @PutMapping("/like")
    public void like(@RequestParam("userId") String userId, @RequestParam("todoId") String todoId){
        toDoService.like(userId, todoId);
    }

    @PutMapping("/dislike")
    public void disLike(@RequestParam("userId") String userId, @RequestParam("todoId") String todoId){
        toDoService.disLike(userId, todoId);
    }

    @PutMapping("/add-todo-to-user-todos")
    public void addToDoToUserToDos(@RequestParam("todoId") String todoId, @RequestParam("userId") String userId){
        toDoService.addToDoToUserToDos(todoId, userId);
    }

    @PutMapping("/save-todo-for-user")
    public void saveToDoForUser(@RequestParam("todoId") String todoId, @RequestParam("userId") String userId){
        toDoService.saveToDoForUser(todoId, userId);
    }
}
