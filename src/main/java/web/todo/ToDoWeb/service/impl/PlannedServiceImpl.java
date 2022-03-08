package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.ToDoFolder;
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
        Set<ToDo> toDos = new HashSet<>();
        User user = userRepository.findByUserName(username).get();
        user.getToDoFolders().forEach(folder ->
                folder.getToDoLists().forEach(toDoList ->
                        toDoList.getToDos().removeIf(toDo ->
                                toDo.getDateTime() == null || toDo.getDateTime().equals("")
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
