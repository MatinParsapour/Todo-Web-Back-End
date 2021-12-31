package web.todo.ToDoWeb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "todo")
public class ToDo {
    private static final String TASK = "task";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String NOTE = "note";
    private static final String STATUS = "status";
    private static final String IS_STARRED = "is_starred";

    @Id
    private String id;

    @Field(name = TASK)
    private String task;

    @Field(name = DATE)
    private Date date;

    @Field(name = TIME)
    private LocalTime time;

    @Field(name = NOTE)
    private String note;

    @Field(name = STATUS)
    private String status;

    @Field(name = IS_STARRED)
    private Boolean isStarred;
}
