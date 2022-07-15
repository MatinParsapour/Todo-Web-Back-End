package web.todo.ToDoWeb.service.impl;

import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.TagRepository;
import web.todo.ToDoWeb.service.TagService;

import java.util.Date;
import java.util.LinkedHashSet;

public class TagServiceImpl extends BaseServiceImpl<Tag, String, TagRepository> implements TagService  {

    public TagServiceImpl(TagRepository repository) {
        super(repository);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public void createTag(String tagName, User user) {
        Tag tag = new Tag();
        tag.setCreatedAt(new Date());
        tag.setCreatedBy(user);
        tag.setName(tagName);
        tag.setToDos(new LinkedHashSet<>());
        save(tag);
    }

    @Override
    public Tag getByName(String name) {
        return repository.findByName(name);
    }
}
