package web.todo.ToDoWeb.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CommentDTO {

    @NotNull(message = "You should enter message")
    @NotBlank(message = "You should enter message")
    @NotEmpty(message = "You should enter message")
    private String message;

    @NotNull(message = "User id is empty")
    @NotBlank(message = "User id is empty")
    @NotEmpty(message = "User id is empty")
    private String userId;

    @NotNull(message = "ToDo id is empty")
    @NotBlank(message = "ToDo id is empty")
    @NotEmpty(message = "ToDo id is empty")
    private String todoId;
}
