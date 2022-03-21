package web.todo.ToDoWeb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = Email.EMAIL_TABLE)
@Data
public class Email {
    public static final String EMAIL_TABLE = "email_table";

    @Id
    private String id;

    private String origin;

    private String destination;

    private String message;

    private Date sentDate = new Date();

    private Boolean isDeleted = false;

}
