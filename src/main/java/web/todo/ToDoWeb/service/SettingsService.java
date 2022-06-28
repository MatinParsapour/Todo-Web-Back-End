package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.dto.UserDTO;

public interface SettingsService {

    UserDTO getUserPersonalInfo(String userId);

    UserDTO getUserSecurityInfo(String userId);

    UserDTO getUserAccountInfo(String userId);
}
