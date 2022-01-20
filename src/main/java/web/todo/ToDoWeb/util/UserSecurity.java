package web.todo.ToDoWeb.util;

import org.springframework.stereotype.Component;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;

@Component
public class UserSecurity {

    private static UserDTO user;

    public static UserDTO getUser() {
        return user;
    }

    public static void setUser(UserDTO user) {
        UserSecurity.user = user;
    }
}