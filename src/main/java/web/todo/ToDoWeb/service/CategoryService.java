package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;

import java.util.List;
import java.util.Set;

public interface CategoryService{

    Set<ToDo> get(String username);
}
