package web.todo.ToDoWeb.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.todo.ToDoWeb.exception.*;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DoplicateException.class)
    public ResponseEntity<String> doplicateExceptionHandler(DoplicateException exception){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmptyException.class)
    public ResponseEntity<String> emptyExceptionHandler(EmptyException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InValidException.class)
    public ResponseEntity<String> invalidExceptionHandler(InValidException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(NotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(WeakException.class)
    public ResponseEntity<String> weakExceptionHandler(WeakException exception){
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> dateTimeParseExceptionHandler(DateTimeParseException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalStateExceptionHandler(IllegalStateException exception){
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(VerificationCodeTimeOutException.class)
    public ResponseEntity<String> verificationCodeTimeOutExceptionHandler(VerificationCodeTimeOutException exception){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BlockedException.class)
    public ResponseEntity<String> blockedExceptionHandler(BlockedException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
