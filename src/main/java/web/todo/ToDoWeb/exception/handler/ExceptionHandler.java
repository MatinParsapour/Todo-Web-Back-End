package web.todo.ToDoWeb.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.todo.ToDoWeb.exception.*;
import web.todo.ToDoWeb.exception.message.ErrorMessage;

import java.time.format.DateTimeParseException;
import java.util.Date;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DoplicateException.class)
    public ResponseEntity<ErrorMessage> doplicateExceptionHandler(DoplicateException exception){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(getErrorMessage(exception,HttpStatus.NOT_ACCEPTABLE));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmptyException.class)
    public ResponseEntity<ErrorMessage> emptyExceptionHandler(EmptyException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorMessage(exception,HttpStatus.BAD_REQUEST));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InValidException.class)
    public ResponseEntity<ErrorMessage> invalidExceptionHandler(InValidException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorMessage(exception,HttpStatus.BAD_REQUEST));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundExceptionHandler(NotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage(exception,HttpStatus.NOT_FOUND));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(WeakException.class)
    public ResponseEntity<ErrorMessage> weakExceptionHandler(WeakException exception){
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(getErrorMessage(exception,HttpStatus.UPGRADE_REQUIRED));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorMessage> dateTimeParseExceptionHandler(DateTimeParseException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorMessage(exception,HttpStatus.BAD_REQUEST));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessage> illegalStateExceptionHandler(IllegalStateException exception){
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(getErrorMessage(exception,HttpStatus.FAILED_DEPENDENCY));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorMessage(exception,HttpStatus.BAD_REQUEST));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(VerificationCodeTimeOutException.class)
    public ResponseEntity<ErrorMessage> verificationCodeTimeOutExceptionHandler(VerificationCodeTimeOutException exception){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(getErrorMessage(exception,HttpStatus.NOT_ACCEPTABLE));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BlockedException.class)
    public ResponseEntity<ErrorMessage> blockedExceptionHandler(BlockedException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(getErrorMessage(exception,HttpStatus.FORBIDDEN));
    }

    private ErrorMessage getErrorMessage(Exception exception, HttpStatus status) {
        return ErrorMessage
                .builder()
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .time(new Date())
                .status(status)
                .build();
    }
}
