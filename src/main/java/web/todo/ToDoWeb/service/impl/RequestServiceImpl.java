package web.todo.ToDoWeb.service.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.Priority;
import web.todo.ToDoWeb.exception.InValidException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.model.RequestId;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.RequestDTO;
import web.todo.ToDoWeb.repository.RequestRepository;
import web.todo.ToDoWeb.service.RequestService;
import web.todo.ToDoWeb.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl extends BaseServiceImpl<Request, String, RequestRepository> implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public RequestServiceImpl(RequestRepository repository, RequestRepository requestRepository, UserService userService, MongoTemplate mongoTemplate) {
        super(repository);
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void startNewRequest(RequestDTO requestDTO) {
        Request request = initializeRequestByRequestDTO(requestDTO);
        save(request);
    }

    @Override
    public List<Request> getUserRequests(String userId) {
        User user = validateAndReturnUser(userId);
        return saveMoreNoticeableInfo(requestRepository.findAllByUserAndIsDeletedFalse(user));
    }

    @Override
    public List<User> getAllUsersRequests() {
        AggregationOperation group = Aggregation.group("user");
        Aggregation aggregation = Aggregation.newAggregation(group);
        List<Request> requests = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Request.class), Request.class).getMappedResults();
        return createUserFromRequestId(requests);
    }

    private List<User> createUserFromRequestId(List<Request> requests) {
        List<User> users = new ArrayList<>();
        requests.forEach(request -> {
            RequestId requestId = extractRequestId(request);
            String userId = extractId(requestId);
            User user = InitializeUser(requestId, userId);
            users.add(user);
        });
        return users;
    }

    private RequestId extractRequestId(Request request) {
        Gson gson = new Gson();
        return gson.fromJson(request.getId(), RequestId.class);
    }

    private String extractId(RequestId requestId) {
        return requestId.get_id().toString().replace("{$oid=", "").replace("}","");
    }

    private User InitializeUser(RequestId requestId, String userId) {
        User user = new User();
        user.setId(userId);
        user.setFirstName(requestId.getFirstName());
        user.setLastName(requestId.getLastName());
        user.setEmail(requestId.getEmail());
        return user;
    }

    @Override
    public Request getRequest(String requestId) {
        Request request = findById(requestId).orElseThrow(() -> new NotFoundException("No request found with provided id"));
        removeUserCrucialInfo(request.getUser());
        return request;
    }

    @Override
    public void deleteRequest(String requestId) {
        Request request = findById(requestId)
                .orElseThrow(() -> new NotFoundException("No request found with provided id"));
        request.setIsDeleted(true);
        save(request);
    }

    @Override
    public void addMessageToRequest(String requestId, Message message) {
        Request request = findById(requestId)
                .orElseThrow(() -> new NotFoundException("No request found with provided id"));
        request.getMessages().add(message);
        save(request);
    }

    @Override
    public void updateRequest(Request request) {
        if (!isRequestExists(request)) {
            throw new NotFoundException("The request not found");
        }

        save(request);
    }

    private Boolean isRequestExists(Request request) {
        return repository.existsByIdAndIsDeletedFalse(request.getId());
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
        return userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user found with provided id"));
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

    private List<Request> saveMoreNoticeableInfo(List<Request> requests){
        return requests
                .stream()
                .peek(request -> {

            removeUserCrucialInfo(request.getUser());

            request.getMessages().removeIf(Message::getIsDeleted);

            List<Message> messages = request.getMessages()
                    .stream()
                    .peek(message ->
                            removeUserCrucialInfo(message.getUser())).collect(Collectors.toList());
            request.setMessages(messages);

        }).collect(Collectors.toList());
    }

    private void removeUserCrucialInfo(User user){
        user.setPassword(null);
        user.setRole(null);
        user.setIsBlocked(null);
        user.setAuthorities(null);
        user.setIsDeleted(null);
        user.setProfileImageUrl(null);
        user.setBirthDay(null);
        user.setPhoneNumber(null);
        user.setToDoFolders(null);
        user.setRegisterDate(null);
        user.setLastLoginDate(null);
        user.setToDos(null);
        user.setSavedToDos(null);
        user.setAge(0);
        user.setToDos(null);
        user.setFollowings(null);
        user.setFollowers(null);
    }
}
