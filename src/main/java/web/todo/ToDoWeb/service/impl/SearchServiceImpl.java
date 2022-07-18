package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.service.SearchService;
import web.todo.ToDoWeb.service.TagService;
import web.todo.ToDoWeb.service.UserService;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final TagService tagService;

    private final UserService userService;

    @Autowired
    public SearchServiceImpl(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    @Override
    public List<Object> search(String keyword) {
        return null;
    }
}
