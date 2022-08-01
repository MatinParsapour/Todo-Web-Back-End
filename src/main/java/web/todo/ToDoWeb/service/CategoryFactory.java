package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDo;

import java.util.Set;

public interface CategoryFactory {

    Set<ToDo> getToDosByCategory(String categoryName, String userId);
}
