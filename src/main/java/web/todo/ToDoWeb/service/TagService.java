package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.TagDTO;

import java.util.List;

public interface TagService {

    boolean existsByName(String name);

    void createTag(String tag, User user);

    Tag getByName(String name);

    void addToDoToTag(ToDo todo, String tagName, User user);

    List<Tag> search(String keyword);

    TagDTO getTagDTO(String name);
}
