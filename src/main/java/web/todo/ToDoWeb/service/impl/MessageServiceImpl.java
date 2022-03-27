package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.MessageDTO;
import web.todo.ToDoWeb.repository.MessageRepository;
import web.todo.ToDoWeb.service.MessageService;
import web.todo.ToDoWeb.service.RequestService;
import web.todo.ToDoWeb.service.UserService;

@Service
public class MessageServiceImpl extends BaseServiceImpl<Message, String, MessageRepository> implements MessageService {

    private final MessageRepository messageRepository;
    private final RequestService requestService;
    private final UserService userService;

    public MessageServiceImpl(MessageRepository repository, MessageRepository messageRepository, RequestService requestService, UserService userService) {
        super(repository);
        this.messageRepository = messageRepository;
        this.requestService = requestService;
        this.userService = userService;
    }

    @Override
    public void sendMessage(String requestId, MessageDTO messageDTO) {
        Message message = initializeMessageByMessageDTO(messageDTO);
        Message newMessage = save(message);
        requestService.addMessageToRequest(requestId, newMessage);
    }

    private Message initializeMessageByMessageDTO(MessageDTO messageDTO){
        Message message = new Message();
        message.setMessage(messageDTO.getMessage());
        message.setUser(validateAndReturnUser(messageDTO.getUserId()));
        return message;
    }

    private User validateAndReturnUser(String userId){
        if (userService.findById(userId).isPresent()){
            return userService.findById(userId).get();
        }
        throw new NotFoundException("No user found");
    }
}
