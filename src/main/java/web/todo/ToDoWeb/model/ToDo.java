package web.todo.ToDoWeb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import web.todo.ToDoWeb.enumeration.Category;
import web.todo.ToDoWeb.enumeration.Status;

import java.util.Date;
import java.util.TreeSet;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "todo")
public class ToDo {
    private static final String TASK = "task";
    private static final String DATE_TIME = "date_time";
    private static final String NOTE = "note";
    private static final String STATUS = "status";
    private static final String IS_STARRED = "is_starred";
    private static final String IS_MY_DAY = "is_my_day";

    @Id
    private String id;

    @Field(name = TASK)
    private String task;

    private String dateTime;

    @Field(name = NOTE)
    private String note = null;

    private Date createdAt = new Date();

    @Field(name = STATUS)
    private Status status = Status.CREATED;

    @Field(name = IS_STARRED)
    private Boolean isStarred = false;

    private Category category;

    private TreeSet<String> pictures = new TreeSet<>();
}
