package web.todo.ToDoWeb.service.impl;

import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.repository.TagRepository;
import web.todo.ToDoWeb.service.TagService;

public class TagServiceImpl extends BaseServiceImpl<Tag, String, TagRepository> implements TagService  {

    public TagServiceImpl(TagRepository repository) {
        super(repository);
    }

    @Override
    public boolean getByName(String name) {
        return repository.existsByName(name);
    }
}
