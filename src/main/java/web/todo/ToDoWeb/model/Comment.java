package web.todo.ToDoWeb.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Comment.COMMENT_DOCUMENT)
@Data
@Builder
public class Comment implements Comparable<Comment> {
    public static final String COMMENT_DOCUMENT = "comment_document";

    @Id
    private String id;

    @DBRef
    private User user;

    private String message;

    @Override
    public int compareTo(Comment o) {
        return this.id.compareTo(o.id);
    }
}
