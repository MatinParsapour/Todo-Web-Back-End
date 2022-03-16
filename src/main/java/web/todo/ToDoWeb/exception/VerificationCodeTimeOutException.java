package web.todo.ToDoWeb.exception;

public class VerificationCodeTimeOutException extends RuntimeException{

    public VerificationCodeTimeOutException(String message) {
        super(message);
    }
}
