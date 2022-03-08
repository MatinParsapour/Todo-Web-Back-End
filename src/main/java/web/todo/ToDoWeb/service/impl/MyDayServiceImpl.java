package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.MyDayService;

import java.util.HashSet;
import java.util.Set;

@Service
public class MyDayServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements MyDayService {

    private final UserRepository userRepository;

    public MyDayServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    public Set<ToDo> get(String username) {
        Set<ToDo> toDos = new HashSet<>();
        User user = userRepository.findByUserName(username).get();
        user.getToDoFolders().forEach(folder ->
                folder.getToDoLists().forEach(toDoList ->
                        toDoList.getToDos().removeIf(toDo ->
                                !toDo.getIsMyDay()
                        )
                )
        );
        user.getToDoFolders().forEach(folder ->
                folder.getToDoLists().forEach(toDoList ->
                        toDoList.getToDos().forEach(toDo ->
                                toDos.add(toDo)
                        )
                )
        );
        return toDos;
    }
}
