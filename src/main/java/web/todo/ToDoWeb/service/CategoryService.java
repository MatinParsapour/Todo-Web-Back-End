package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;

public interface CategoryService<T extends Category>{

    void add(ToDo toDo, User user);

    T get(User user);
}
