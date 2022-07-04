package web.todo.ToDoWeb.exception.message;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Builder
public class ErrorMessage {
    private final String message;
    private final String type;
    private final Date time;
    private final HttpStatus status;
}
