package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
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
        User user = userRepository.findByUserName(username).get();
        user.getToDoFolders().forEach(folder ->
                folder.getToDoLists().forEach(toDoList ->
                        toDoList.getToDos().removeIf(toDo ->
                                toDo.getIsMyDay() || toDo.getDateTime() != null
                        )
                )
        );
        user.getToDoFolders().forEach(folder ->
                folder.getToDoLists().forEach(toDoList ->
                        toDos.addAll(toDoList.getToDos())
                )
        );
        return toDos;
    }
}
