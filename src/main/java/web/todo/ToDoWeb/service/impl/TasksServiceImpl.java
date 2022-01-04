package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Tasks;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.TasksRepository;
import web.todo.ToDoWeb.service.TasksService;

@Service
public class TasksServiceImpl extends BaseServiceImpl<Tasks, String, TasksRepository> implements TasksService {

    public TasksServiceImpl(TasksRepository repository) {
        super(repository);
    }

    @Override
    public void add(ToDo toDo, User user) {
        Tasks tasks = new Tasks();
        tasks.getToDos().add(toDo);
        tasks.setUser(user);
        save(tasks);
    }

    @Override
    public Tasks get(User user) {
        return null;
    }
}
