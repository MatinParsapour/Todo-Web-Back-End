package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.MyDay;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.MyDayRepository;
import web.todo.ToDoWeb.service.MyDayService;

import java.util.Set;

@Service
public class MyDayServiceImpl extends BaseServiceImpl<MyDay, String, MyDayRepository> implements MyDayService {

    public MyDayServiceImpl(MyDayRepository repository) {
        super(repository);
    }

    @Override
    public void add(ToDo toDo, User user) {
        MyDay myDay = new MyDay();
        myDay.getToDos().add(toDo);
        myDay.setUser(user);
        save(myDay);
    }

    @Override
    public MyDay get(User user) {
        return null;
    }
}
