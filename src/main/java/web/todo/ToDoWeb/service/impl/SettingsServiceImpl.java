package web.todo.ToDoWeb.service.impl;

import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.SettingsService;

public class SettingsServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements SettingsService {

    public SettingsServiceImpl(UserRepository repository) {
        super(repository);
    }
}
