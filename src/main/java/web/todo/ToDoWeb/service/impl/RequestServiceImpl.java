package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.repository.RequestRepository;
import web.todo.ToDoWeb.service.RequestService;

@Service
public class RequestServiceImpl extends BaseServiceImpl<Request, String, RequestRepository> implements RequestService {

    public RequestServiceImpl(RequestRepository repository) {
        super(repository);
    }

    private User validateAndReturnUser(String userId){
        if (userService.findById(userId).isPresent()){
            return userService.findById(userId).get();
        }
        throw new NotFoundException("No user found");
    }

    private Priority returnPriority(String priority){
        switch (priority.toLowerCase()){
            case "low":
                return Priority.LOW;
            case "medium":
                return Priority.MEDIUM;
            case "high":
                return Priority.HIGH;
            default:
                throw new InValidException("the priority entered is wrong");
        }
    }
}
