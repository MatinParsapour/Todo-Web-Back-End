package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.Category;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.PlannedService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlannedServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements PlannedService {

    private final UserRepository userRepository;

    public PlannedServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }


    @Override
    public Set<ToDo> get(String username) {
        User user = userRepository.findByUserNameAndIsDeletedFalse(username)
                .orElseThrow(() -> new NotFoundException("No user found with provided username"));

        return user.getToDos()
                .stream()
                .peek(toDo -> {
                    toDo.setComments(null);
                    toDo.setLikes(null);
                }).filter(toDo -> toDo.getCategory().equals(Category.PLANNED))
                .collect(Collectors.toSet());
    }
}
