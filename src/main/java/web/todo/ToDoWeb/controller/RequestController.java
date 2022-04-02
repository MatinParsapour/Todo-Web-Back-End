package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.RequestDTO;
import web.todo.ToDoWeb.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/start-new-request")
    public void startNewRequest(@Valid @RequestBody RequestDTO requestDTO){
        requestService.startNewRequest(requestDTO);
    }

    @GetMapping("/get-user-requests/{userId}")
    public List<Request> getUserRequests(@PathVariable("userId") String userId){
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/get-all-users-requests")
    public List<User> getAllUsersRequests(){
        return requestService.getAllUsersRequests();
    }

    @GetMapping("/get-request/{requestId}")
    public Request getRequest(@PathVariable("requestId") String requestId){
        return requestService.getRequest(requestId);
    }

    @DeleteMapping("/delete-request/{requestId}")
    public void deleteRequest(@PathVariable("requestId") String requestId){
        requestService.deleteRequest(requestId);
    }

    @PutMapping("/update-request")
    public void updateRequest(@RequestBody Request request){
        requestService.updateRequest(request);
    }
}
