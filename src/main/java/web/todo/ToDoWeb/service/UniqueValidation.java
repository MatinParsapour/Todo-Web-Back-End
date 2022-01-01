package web.todo.ToDoWeb.service;

public interface UniqueValidation {

    /**
     * Check if username is doplicate or not
     * @param userName the username user provided
     * @return true if the username is doplicate
     */
    Boolean existsByUserName(String userName);

    /**
     * Check if email is doplicate or not
     * @param email the email user provided
     * @return true if the email is doplicate
     */
    Boolean existsByEmail(String email);


}
