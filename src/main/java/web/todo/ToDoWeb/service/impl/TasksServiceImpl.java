package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.Category;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.TasksService;

import java.util.HashSet;
import java.util.Set;

@Service
public class TasksServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements TasksService {

    private final UserRepository userRepository;

    public TasksServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }


    @Override
    public Set<ToDo> get(String username) {
        Set<ToDo> toDos = new HashSet<>();
        User user = userRepository.findByIdAndIsDeletedFalse(username).get();
        user.getToDos().removeIf(toDo ->
                !toDo.getCategory().equals(Category.TASKS)
        );
        user.getToDos().forEach(
                toDo -> toDos.add(toDo)
        );
        return toDos;
    }
}
