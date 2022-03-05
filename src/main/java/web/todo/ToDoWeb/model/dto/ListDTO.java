package web.todo.ToDoWeb.model.dto;

import lombok.Data;

@Data
public class ListDTO {

    private String username;

    private String listName;

    private String folderName;

    private String oldListName;

    private String newListName;
}
