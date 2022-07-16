package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.TagRepository;
import web.todo.ToDoWeb.service.TagService;

import java.util.Date;
import java.util.LinkedList;

@Service
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

    @Override
    public void addToDoToTag(ToDo todo, String tagName, User user) {
        if (!existsByName(tagName)) {
            createTag(tagName, user);
        }

        insertToDoInTag(todo, tagName);
    }

    private void insertToDoInTag(ToDo todo, String tagName) {
        Tag tag = getByName(tagName);
        tag.getToDos().add(todo);
        save(tag);
    }
}
