package web.todo.ToDoWeb.util;

import org.springframework.stereotype.Component;
import web.todo.ToDoWeb.model.User;

@Component
public class UserSecurity {

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserSecurity.user = user;
    }
}