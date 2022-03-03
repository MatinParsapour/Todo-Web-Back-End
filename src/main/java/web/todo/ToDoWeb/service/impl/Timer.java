package web.todo.ToDoWeb.service.impl;

import java.util.Date;

public class Timer implements Runnable{
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        while (elapsedTime < 2*60*1000) {
            elapsedTime = (new Date()).getTime() - startTime;
        }
        EmailConfirmationServiceImpl.emptyEmailField();
    }
}
