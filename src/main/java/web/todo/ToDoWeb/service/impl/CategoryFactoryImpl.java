package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.InValidException;
import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.service.CategoryFactory;
import web.todo.ToDoWeb.service.MyDayService;
import web.todo.ToDoWeb.service.PlannedService;
import web.todo.ToDoWeb.service.TasksService;

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

    public void addToCategory(ToDo toDo, User user){
        if (toDo.getDateTime() != null){
            plannedService.add(toDo, user);
        }else if (toDo.getIsMyDay() != null && toDo.getIsMyDay()){
            myDayService.add(toDo, user);
        } else {
            tasksService.add(toDo, user);
        }
    }

    public Category getToDosByCategory(String categoryName, User user){
        if (categoryName.equalsIgnoreCase("MyDay")){
            return myDayService.get(user);
        }else if (categoryName.equalsIgnoreCase("Planned")){
            return plannedService.get(user);
        }else if (categoryName.equalsIgnoreCase("Tasks")){
            return tasksService.get(user);
        } else {
            throw new InValidException("The category name is in valid");
        }
    }
}
