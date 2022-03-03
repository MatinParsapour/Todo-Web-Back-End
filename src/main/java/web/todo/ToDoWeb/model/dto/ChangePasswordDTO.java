package web.todo.ToDoWeb.model.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {

    private String email;

    private String password;

    private String reTypePassword;
}
