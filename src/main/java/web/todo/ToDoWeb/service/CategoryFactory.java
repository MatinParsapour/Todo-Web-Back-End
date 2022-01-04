package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.User;

import java.util.List;

public interface CategoryFactory {

    /**
     * Get to do and user and check
     * If had date and time save in planned
     * If is My day save in MyDay
     * else save in tasks
     * @param toDo the to do saved in database
     * @param user the user to do belongs to
     */
    void addToCategory(ToDo toDo, User user);

    /**
     * Check by category name and return proper data
     * @param categoryName name of category : MyDay, Planned, Tasks
     * @param user the user that saved in database
     * @return the category found with provided information
     */
    List<Category> getToDosByCategory(String categoryName, User user);
}
