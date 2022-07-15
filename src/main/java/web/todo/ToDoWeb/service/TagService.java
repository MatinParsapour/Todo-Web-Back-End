package web.todo.ToDoWeb.service;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.User;

@Service
public interface TagService {

    boolean existsByName(String name);

    void createTag(String tag, User user);
}
