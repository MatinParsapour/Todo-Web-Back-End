package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.Category;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.MyDayService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyDayServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements MyDayService {

    private final UserRepository userRepository;

    public MyDayServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    public Set<ToDo> get(String username) {
        User user = userRepository.findByUserNameAndIsDeletedFalse(username).orElseThrow(() -> new NotFoundException("No user found with provided username"));

        return user.getToDos()
                .stream()
                .peek(toDo -> {
                    toDo.setComments(null);
                    toDo.setLikes(null);
                }).filter(toDo -> toDo.getCategory().equals(Category.MYDAY))
                .collect(Collectors.toSet());
    }
}
