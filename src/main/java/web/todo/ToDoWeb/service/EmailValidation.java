package web.todo.ToDoWeb.service;

public interface EmailValidation {

    /**
     * Check if email is valid like ******@****.***
     * @param email check the email provided
     * @return true if the email is valid
     */
    Boolean isEmailValid(String email);
}
