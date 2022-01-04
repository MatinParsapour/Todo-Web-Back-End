package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.Tasks;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.TasksRepository;
import web.todo.ToDoWeb.service.TasksService;

import java.util.List;

@Service
public class TasksServiceImpl extends BaseServiceImpl<Tasks, String, TasksRepository> implements TasksService {

    private final TasksRepository tasksRepository;

    public TasksServiceImpl(TasksRepository repository, TasksRepository tasksRepository) {
        super(repository);
        this.tasksRepository = tasksRepository;
    }

    @Override
    public void add(ToDo toDo, User user) {
        Tasks tasks = new Tasks();
        tasks.getToDos().add(toDo);
        tasks.setUser(user);
        save(tasks);
    }

    @Override
    public List<Category> get(User user) {
        return tasksRepository.findAllByUser(user);
    }
}
