package web.todo.ToDoWeb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Request.REQUEST_TABLE)
public class Request {

    public static final String REQUEST_TABLE = "request_table";

    @Id
    private String id;
}
