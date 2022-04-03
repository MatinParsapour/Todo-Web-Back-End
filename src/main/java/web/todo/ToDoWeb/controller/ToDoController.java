package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.service.ToDoService;
import web.todo.ToDoWeb.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    /**
     * Add to do to the list
     * @param toDoId That at least must include task
     * @param listName name of list to do add to it
     * @param folderName name of folder list belongs to
     * @param userId username of user folder belongs to
     */
    @PostMapping("/add-to-do/{todoId}/list/{listName}/folder/{folderName}/for/{userId}")
    public void addToDoToList(@PathVariable("todoId") String toDoId, @PathVariable("listName") String listName, @PathVariable("folderName") String folderName, @PathVariable("userId") String userId){
        toDoService.saveToDoInList(toDoId, listName, folderName, userId);
    }

    /**
     * Add to do to the specific category
     * @param toDo that user created at least have task
     * @param userId username of user to do belongs to
     */
    @PostMapping("/add-to-do/{userId}")
    public void addToDoToCategory(@RequestBody ToDo toDo, @PathVariable("userId") String userId){
        toDoService.saveToDoInCategory(toDo, userId);
    }

    /**
     * Update to do
     * @param toDo that at least include task
     */
    @PutMapping("/update-to-do")
    public void updateToDo(@RequestBody ToDo toDo){
        toDoService.updateToDo(toDo);
    }

    @DeleteMapping("/delete-to-do/{folderName}/{listName}/{userId}/{toDoId}")
    public void deleteToDo(@PathVariable("folderName") String folderName, @PathVariable("listName") String listName, @PathVariable("userId") String userId, @PathVariable("toDoId") String toDoId){
        toDoService.deleteToDo(folderName, listName, userId, toDoId);
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
        return toDoService.findById(toDoId).get();
    }

    @DeleteMapping("/delete-photo/{toDoId}/{pictureName}")
    public void deleteToDoPicture(@PathVariable("toDoId") String toDoId, @PathVariable("pictureName") String pictureName) throws IOException {
        toDoService.deleteToDoPicture(toDoId, pictureName);
    }
}
