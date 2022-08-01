package web.todo.ToDoWeb.constants;

public class Authority {
    public static final String[] USER_AUTHORITY = {"todo:create", "todo:update", "todo:delete", "todo:read"};
    public static final String[] MANAGER_AUTHORITY = {"todo:create", "todo:update", "todo:delete", "todo:read", "user:read", "user:update"};
    public static final String[] ADMIN_AUTHORITY = {"todo:create", "todo:update", "todo:delete", "todo:read", "user:read", "user:update", "user:create"};
    public static final String[] SUPER_ADMIN_AUTHORITY = {"todo:create", "todo:update", "todo:delete", "todo:read", "user:read", "user:update", "user:create", "user:delete"};
}
