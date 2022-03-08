package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;

import java.util.List;
import java.util.Set;

public interface CategoryFactory {

    /**
     * Check by category name and return proper data
     * @param categoryName name of category : MyDay, Planned, Tasks
     * @param user the user that saved in database
     * @return the category found with provided information
     */
    Set<ToDo> getToDosByCategory(String categoryName, String username);
}
