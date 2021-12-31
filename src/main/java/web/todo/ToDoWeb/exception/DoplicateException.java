package web.todo.ToDoWeb.exception;

public class DoplicateException extends RuntimeException {

    public DoplicateException(String message) {
        super(message);
    }
}
