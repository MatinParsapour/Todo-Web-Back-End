package web.todo.ToDoWeb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

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

    private String dateTime = null;

    @Field(name = NOTE)
    private String note = null;

    @Field(name = STATUS)
    private String status = null;

    @Field(name = IS_STARRED)
    private Boolean isStarred = false;

    @Field(name = IS_MY_DAY)
    private Boolean isMyDay = false;
}
