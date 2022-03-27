package web.todo.ToDoWeb.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MessageDTO {

    @NotNull(message = "You should enter user id")
    @NotEmpty(message = "You should enter user id")
    @NotBlank(message = "You should enter user id")
    private String userId;

    @NotEmpty(message = "You should enter message")
    @NotBlank(message = "You should enter message")
    @NotNull(message = "You should enter message")
    private String message;
}
