package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.MyDay;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.MyDayRepository;
import web.todo.ToDoWeb.service.MyDayService;

import java.util.List;

@Service
public class MyDayServiceImpl extends BaseServiceImpl<MyDay, String, MyDayRepository> implements MyDayService {

    private final MyDayRepository myDayRepository;

    public MyDayServiceImpl(MyDayRepository repository, MyDayRepository myDayRepository) {
        super(repository);
        this.myDayRepository = myDayRepository;
    }

    @Override
    public void add(ToDo toDo, User user) {
        MyDay myDay = new MyDay();
        myDay.getToDos().add(toDo);
        myDay.setUser(user);
        save(myDay);
    }

    @Override
    public List<Category> get(User user) {
        return myDayRepository.findAllByUser(user);
    }
}
