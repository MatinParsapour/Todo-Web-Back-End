package web.todo.ToDoWeb.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.model.dto.MessageDTO;
import web.todo.ToDoWeb.service.MessageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static web.todo.ToDoWeb.constants.FileConstants.FORWARD_SLASH;
import static web.todo.ToDoWeb.constants.FileConstants.REQUEST_FOLDER;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send-message/{requestId}")
    public void sendMessage(@PathVariable("requestId") String requestId, @RequestBody MessageDTO messageDTO){
        messageService.sendMessage(requestId, messageDTO);
    }

    @PostMapping("/send-picture")
    public void sendPicture(@RequestParam("requestId") String requestId, @RequestParam("picture")MultipartFile picture, @RequestParam("userId") String userId) throws IOException {
        messageService.sendPicture(requestId, picture, userId);
    }

    @GetMapping(value = "/image/{userId}/{requestId}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getPicture(@PathVariable("userId") String userId, @PathVariable("requestId") String requestId, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(REQUEST_FOLDER + FORWARD_SLASH + userId + FORWARD_SLASH + requestId + FORWARD_SLASH + fileName));
    }

    @PutMapping("/update-message")
    public void updateMessage(@RequestBody Message message){
        messageService.updateMessage(message);
    }

    @DeleteMapping("/delete-message/{messageId}")
    public void deleteMessage(@PathVariable("messageId") String messageId){
        messageService.deleteMessage(messageId);
    }
}
