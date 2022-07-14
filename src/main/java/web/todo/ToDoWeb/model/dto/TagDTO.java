package web.todo.ToDoWeb.model.dto;

import lombok.Getter;
import lombok.Setter;
import web.todo.ToDoWeb.model.ToDo;

import java.util.Set;

@Setter
@Getter
public class TagDTO {

    private String id;

    private String name;

    private Set<ToDo> toDos;
}
