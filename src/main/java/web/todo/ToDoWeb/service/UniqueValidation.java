package web.todo.ToDoWeb.service;

public interface UniqueValidation {

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(Long phoneNumber);

}
