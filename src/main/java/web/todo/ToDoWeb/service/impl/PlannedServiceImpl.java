package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.Category;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.PlannedService;

import java.util.HashSet;
import java.util.Set;

@Service
public class PlannedServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements PlannedService {

    private final UserRepository userRepository;

    public PlannedServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }


    @Override
    public Set<ToDo> get(String username) {
        User user = userRepository.findByIdAndIsDeletedFalse(username).get();
        user.getToDos().removeIf(toDo ->
                !toDo.getCategory().equals(Category.PLANNED)
        );
        user.getToDos().forEach(todo -> {
            todo.setComments(null);
            todo.setLikes(null);
        });
        return new HashSet<>(user.getToDos());
    }
}
