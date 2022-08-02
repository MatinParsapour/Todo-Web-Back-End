package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.service.CodeGeneratorService;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    private static final String ALGORITHM = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";


    @Override
    public String generateString() {
        StringBuilder token = new StringBuilder();
        int i = 0;
        do {
            int position = ThreadLocalRandom.current().nextInt(0, ALGORITHM.length() - 1);
            char character = ALGORITHM.charAt(position);
            token.append(character);
            i++;
        } while (i <= 40);
        return token.toString();
    }

    @Override
    public Integer generateNumber() {
        return ThreadLocalRandom.current().nextInt(10000, 99999);
    }
}
