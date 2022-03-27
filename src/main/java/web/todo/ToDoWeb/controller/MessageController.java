package web.todo.ToDoWeb.controller;

import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.model.dto.MessageDTO;
import web.todo.ToDoWeb.service.MessageService;

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

    @PutMapping("/update-message")
    public void updateMessage(@RequestBody Message message){
        messageService.updateMessage(message);
    }
}
