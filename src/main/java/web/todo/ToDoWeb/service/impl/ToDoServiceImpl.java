package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.ToDoRepository;
import web.todo.ToDoWeb.service.CategoryFactory;
import web.todo.ToDoWeb.service.FilledValidation;
import web.todo.ToDoWeb.service.ListService;
import web.todo.ToDoWeb.service.ToDoService;

@Service
public class ToDoServiceImpl extends BaseServiceImpl<ToDo, String, ToDoRepository> implements ToDoService, FilledValidation {

    private final ToDoRepository toDoRepository;
    private final ListService listService;
    private final CategoryFactory categoryFactory;

    @Autowired
    public ToDoServiceImpl(ToDoRepository repository, ToDoRepository toDoRepository, ListService listService, CategoryFactory categoryFactory) {
        super(repository);
        this.toDoRepository = toDoRepository;
        this.listService = listService;
        this.categoryFactory = categoryFactory;
    }

    @Override
    public void saveToDoInList(ToDo toDo, String listName, String folderName, String username) {
        if (isEmpty(toDo.getTask()) || isBlank(toDo.getTask()) || isNull(toDo.getTask()) || isWhiteSpace(toDo.getTask())){
            throw new EmptyException("For to do at least you should fill task");
        }
        ToDo savedToDo = save(toDo);
        listService.insertToDoToList(savedToDo,listName,folderName,username);
    }

    @Override
    public void updateToDo(ToDo toDo) {
        if (isEmpty(toDo.getTask()) || isBlank(toDo.getTask()) || isNull(toDo.getTask()) || isWhiteSpace(toDo.getTask())){
            throw new EmptyException("For to do at least you should fill task");
        }
        if (isEmpty(toDo.getId()) || isBlank(toDo.getId()) || isNull(toDo.getId()) || isWhiteSpace(toDo.getId())){
            throw new EmptyException("The to do must have id");
        }
        save(toDo);
    }

    @Override
    public void deleteToDo(String folderName, String listName, String userName, String toDoId) {
        if (isEmpty(toDoId) || isBlank(toDoId) || isNull(toDoId) || isWhiteSpace(toDoId)){
            throw new EmptyException("The id field is empty");
        }
        listService.removeToDoFromList(folderName, listName, userName, toDoId);
        deleteById(toDoId);
    }

    @Override
    public void saveToDoInCategory(ToDo toDo, User user) {
        if (isEmpty(toDo.getTask()) || isBlank(toDo.getTask()) || isNull(toDo.getTask()) || isWhiteSpace(toDo.getTask())){
            throw new EmptyException("For to do at least you should fill task");
        }
        ToDo savedToDo = save(toDo);
        categoryFactory.addToCategory(savedToDo, user);
    }


    @Override
    public Boolean isEmpty(String field) {
        return field.isEmpty();
    }

    @Override
    public Boolean isBlank(String field) {
        return field.isBlank();
    }

    @Override
    public Boolean isNull(String field) {
        return field == null;
    }

    @Override
    public Boolean isWhiteSpace(String field) {
        return field.trim().isEmpty();
    }
}

