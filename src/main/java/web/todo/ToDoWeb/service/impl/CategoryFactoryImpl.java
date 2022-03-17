package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.InValidException;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.service.CategoryFactory;
import web.todo.ToDoWeb.service.MyDayService;
import web.todo.ToDoWeb.service.PlannedService;
import web.todo.ToDoWeb.service.TasksService;

import java.util.List;
import java.util.Set;

@Service
public class CategoryFactoryImpl implements CategoryFactory {

    private final MyDayService myDayService;
    private final TasksService tasksService;
    private final PlannedService plannedService;

    @Autowired
    public CategoryFactoryImpl(MyDayService myDayService, TasksService tasksService, PlannedService plannedService) {
        this.myDayService = myDayService;
        this.tasksService = tasksService;
        this.plannedService = plannedService;
    }

    @Override
    public Set<ToDo> getToDosByCategory(String categoryName, String userId){
        if (categoryName.equalsIgnoreCase("MyDay")){
            return myDayService.get(userId);
        }else if (categoryName.equalsIgnoreCase("Planned")){
            return plannedService.get(userId);
        }else if (categoryName.equalsIgnoreCase("Tasks")){
            return tasksService.get(userId);
        } else {
            throw new InValidException("The category name is invalid");
        }
    }
}
