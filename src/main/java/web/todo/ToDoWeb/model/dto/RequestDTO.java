package web.todo.ToDoWeb.model.dto;

import lombok.Data;
import web.todo.ToDoWeb.enumeration.Priority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RequestDTO {

    @NotEmpty(message = "You should fill topic")
    @NotBlank(message = "You should fill topic")
    @NotNull(message = "You should fill topic")
    private String topic;

    @NotBlank(message = "You should fill priority")
    @NotEmpty(message = "You should fill priority")
    @NotNull(message = "You should fill priority")
    private String priority;

    @NotBlank(message = "You should enter userId")
    @NotEmpty(message = "You should enter userId")
    @NotNull(message = "You should enter userId")
    private String userId;
}
