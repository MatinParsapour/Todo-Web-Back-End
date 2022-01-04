package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.Planned;

public interface PlannedService extends BaseService<Planned, String> , CategoryService<Category> {
}
