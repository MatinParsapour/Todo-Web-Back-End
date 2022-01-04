package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.Planned;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.PlannedRepository;
import web.todo.ToDoWeb.service.PlannedService;

import java.util.List;

@Service
public class PlannedServiceImpl extends BaseServiceImpl<Planned, String, PlannedRepository> implements PlannedService {

    private final PlannedRepository plannedRepository;

    public PlannedServiceImpl(PlannedRepository repository, PlannedRepository plannedRepository) {
        super(repository);
        this.plannedRepository = plannedRepository;
    }

    @Override
    public void add(ToDo toDo, User user) {
        Planned planned = new Planned();
        planned.getToDos().add(toDo);
        planned.setUser(user);
        save(planned);
    }

    @Override
    public List<Category> get(User user) {
        return plannedRepository.findAllByUser(user);
    }
}
