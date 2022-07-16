package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;

public interface TagService {

    boolean existsByName(String name);

    void createTag(String tag, User user);

    Tag getByName(String name);

    void addToDoToTag(ToDo todo, String tagName, User user);
}
