package web.todo.ToDoWeb.service;

import org.springframework.web.multipart.MultipartFile;
import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.model.dto.MessageDTO;

import java.io.IOException;

public interface MessageService extends BaseService<Message, String> {

    void sendMessage(String requestId, MessageDTO messageDTO);

    void updateMessage(Message message);

    void deleteMessage(String messageId);

    void sendPicture(String requestId, MultipartFile picture, String userId) throws IOException;
}
