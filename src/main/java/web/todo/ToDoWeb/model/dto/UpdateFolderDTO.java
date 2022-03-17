package web.todo.ToDoWeb.model.dto;

import lombok.Data;

@Data
public class UpdateFolderDTO {

    private String userId;

    private String oldName;

    private String newName;
}
