package web.todo.ToDoWeb.model.dto;

import lombok.Data;

@Data
public class UpdateFolderDTO {

    private String username;

    private String oldName;

    private String newName;
}
