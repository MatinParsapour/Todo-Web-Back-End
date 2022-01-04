package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Planned;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.PlannedRepository;
import web.todo.ToDoWeb.service.PlannedService;

@Service
public class PlannedServiceImpl extends BaseServiceImpl<Planned, String, PlannedRepository> implements PlannedService {
    public PlannedServiceImpl(PlannedRepository repository) {
        super(repository);
    }

    @Override
    public void add(ToDo toDo, User user) {
        Planned planned = new Planned();
        planned.getToDos().add(toDo);
        planned.setUser(user);
        save(planned);
    }

    @Override
    public Planned get(User user) {
        return null;
    }
}
