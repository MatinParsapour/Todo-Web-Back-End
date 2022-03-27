package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.repository.MessageRepository;
import web.todo.ToDoWeb.service.MessageService;

@Service
public class MessageServiceImpl extends BaseServiceImpl<Message, String, MessageRepository> implements MessageService {

    public MessageServiceImpl(MessageRepository repository) {
        super(repository);
    }
}
