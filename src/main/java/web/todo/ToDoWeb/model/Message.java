package web.todo.ToDoWeb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Message.MESSAGE_TABLE)
public class Message {

    public static final String MESSAGE_TABLE = "message_table";

    @Id
    private String id;
}
