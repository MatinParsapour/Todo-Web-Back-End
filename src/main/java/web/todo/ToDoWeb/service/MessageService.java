package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.model.dto.MessageDTO;

public interface MessageService extends BaseService<Message, String> {

    void sendMessage(String requestId, MessageDTO messageDTO);

    void updateMessage(Message message);

    void deleteMessage(String messageId);
}
