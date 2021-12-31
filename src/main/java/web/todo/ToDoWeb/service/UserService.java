package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserSignUpDTO;

public interface UserService extends BaseService<User, String> {

    User saveDTO(UserSignUpDTO userSignUpDTO);
}
