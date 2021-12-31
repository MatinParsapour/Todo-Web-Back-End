package web.todo.ToDoWeb.service;

public interface UniqueValidation {

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);


}
