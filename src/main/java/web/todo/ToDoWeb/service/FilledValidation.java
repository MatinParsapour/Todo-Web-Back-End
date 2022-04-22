package web.todo.ToDoWeb.service;

public interface FilledValidation {

    Boolean isEmpty(String field);

    Boolean isBlank(String field);

    Boolean isNull(String field);

    Boolean isWhiteSpace(String field);

}
