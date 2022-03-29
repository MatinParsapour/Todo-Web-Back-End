package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.Priority;
import web.todo.ToDoWeb.exception.InValidException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.RequestDTO;
import web.todo.ToDoWeb.repository.RequestRepository;
import web.todo.ToDoWeb.service.RequestService;
import web.todo.ToDoWeb.service.UserService;

import java.util.List;

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

    @Override
    public List<Request> getUserRequests(String userId) {
        User user = validateAndReturnUser(userId);
        return makeImportantPropertiesOfRequestsNull(requestRepository.findAllByUserAndIsDeletedFalse(user));
    }

    @Override
    public List<Request> getAllUsersRequests() {
        return makeImportantPropertiesOfRequestsNull(findAll());
    }

    @Override
    public Request getRequest(String requestId) {
        if (requestRepository.existsByIdAndIsDeletedFalse(requestId)){
            return makeImportantPropertiesOfRequestNull(findById(requestId).get());
        }
        throw new NotFoundException("This request might be deleted");
    }

    @Override
    public void deleteRequest(String requestId) {
        if (requestRepository.existsByIdAndIsDeletedFalse(requestId)){
            Request request = findById(requestId).get();
            request.setIsDeleted(true);
            save(request);
        } else {
            throw new NotFoundException("The request might be deleted");
        }
    }

    @Override
    public void addMessageToRequest(String requestId, Message message) {
        if (requestRepository.existsByIdAndIsDeletedFalse(requestId)){
            Request request = findById(requestId).get();
            request.getMessages().add(message);
            save(request);
        } else {
            throw new NotFoundException("The request session might be deleted");
        }
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

    private List<Request> makeImportantPropertiesOfRequestsNull(List<Request> requests){
        requests.forEach(this::makeImportantPropertiesOfRequestNull);
        return requests;
    }

    private Request makeImportantPropertiesOfRequestNull(Request request){
        request.getUser().setPassword(null);
        request.getUser().setRole(null);
        request.getUser().setIsBlocked(null);
        request.getUser().setAuthorities(null);
        request.getUser().setValidatorEmail(null);
        request.getUser().setIsDeleted(null);
        request.getUser().setProfileImageUrl(null);
        request.getUser().setBirthDay(null);
        request.getUser().setPhoneNumber(null);
        request.getUser().setToDoFolders(null);
        request.getUser().setAge(0);
        request.getMessages().removeIf(Message::getIsDeleted);
        request.getMessages().forEach(this::makeImportantPropertiesOfMessageNull);
        return request;
    }

    private Message makeImportantPropertiesOfMessageNull(Message message){
        message.getUser().setPassword(null);
        message.getUser().setRole(null);
        message.getUser().setIsBlocked(null);
        message.getUser().setAuthorities(null);
        message.getUser().setValidatorEmail(null);
        message.getUser().setIsDeleted(null);
        message.getUser().setProfileImageUrl(null);
        message.getUser().setBirthDay(null);
        message.getUser().setPhoneNumber(null);
        message.getUser().setToDoFolders(null);
        message.getUser().setAge(0);
        return message;
    }
}
