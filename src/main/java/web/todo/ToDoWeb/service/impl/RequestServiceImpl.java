package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.Priority;
import web.todo.ToDoWeb.exception.InValidException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.RequestDTO;
import web.todo.ToDoWeb.repository.RequestRepository;
import web.todo.ToDoWeb.service.RequestService;
import web.todo.ToDoWeb.service.UserService;

@Service
public class RequestServiceImpl extends BaseServiceImpl<Request, String, RequestRepository> implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;

    public RequestServiceImpl(RequestRepository repository, RequestRepository requestRepository, UserService userService) {
        super(repository);
        this.requestRepository = requestRepository;
        this.userService = userService;
    }

    @Override
    public void startNewRequest(RequestDTO requestDTO) {
        Request request = initializeRequestByRequestDTO(requestDTO);
        save(request);
    }

    private Request initializeRequestByRequestDTO(RequestDTO requestDTO){
        Request request = new Request();
        request.setTopic(requestDTO.getTopic());
        request.setPriority(returnPriority(requestDTO.getPriority()));
        User user = validateAndReturnUser(requestDTO.getUserId());
        request.setUser(user);
        return request;
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
