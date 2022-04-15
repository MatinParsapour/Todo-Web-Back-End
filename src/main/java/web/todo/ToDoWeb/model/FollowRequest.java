package web.todo.ToDoWeb.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import web.todo.ToDoWeb.enumeration.FollowRequestStatus;

@Document(collection = FollowRequest.FOLLOW_REQUEST_TABLE)
@Data
public class FollowRequest {
    public static final String FOLLOW_REQUEST_TABLE = "follow_request";

    @DBRef
    private User applicant;

    @DBRef
    private User responder;

    private FollowRequestStatus status;
}
