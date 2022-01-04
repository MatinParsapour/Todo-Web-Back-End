package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.Tasks;

public interface TasksService extends BaseService<Tasks, String> , CategoryService<Category> {
}
