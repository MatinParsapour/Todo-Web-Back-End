package web.todo.ToDoWeb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import web.todo.ToDoWeb.enumeration.Priority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = Request.REQUEST_TABLE)
@Data
public class Request {

    public static final String REQUEST_TABLE = "request_table";

    @Id
    private String id;

    private Date startsAt = new Date();

    private Boolean isFinished = false;

    private Boolean isSolved = false;

    private Date finishedAt;

    private String topic;

    private Priority priority;

    @DBRef
    private List<Message> messages = new ArrayList<>();

    private Boolean isDeleted;
}
