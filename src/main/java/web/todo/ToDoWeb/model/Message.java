package web.todo.ToDoWeb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = Message.MESSAGE_TABLE)
@Data
public class Message {

    public static final String MESSAGE_TABLE = "message_table";

    @Id
    private String id;

    @DBRef
    private User user;

    private String message;

    private Date sentAt;

    private Boolean isDeleted;
}
