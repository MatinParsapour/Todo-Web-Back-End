package web.todo.ToDoWeb.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Builder
@Document(collection = Tag.COLLECTION_NAME)
public class Tag {
    public static final String COLLECTION_NAME = "tag";

    @Id
    private String id;

    private String name;

    private Date createdAt;

    @DBRef
    private User createdBy;

    @DBRef
    private Set<ToDo> toDos;
}
