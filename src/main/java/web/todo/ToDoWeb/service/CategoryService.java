package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDo;

import java.util.Set;

public interface CategoryService {

    Set<ToDo> get(String username);
}
