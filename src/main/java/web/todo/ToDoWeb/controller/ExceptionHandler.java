package web.todo.ToDoWeb.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.todo.ToDoWeb.exception.DoplicateException;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.exception.UnValidException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DoplicateException.class)
    public String doplicateExceptionHandling(DoplicateException doplicateException){
        return doplicateException.getMessage();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmptyException.class)
    public String emptyExceptionHandler(EmptyException emptyException){
        return emptyException.getMessage();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnValidException.class)
    public String unValidExceptionHandler(UnValidException unValidException){
        return unValidException.getMessage();
    }
}
