package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.todo.ToDoWeb.model.dto.RequestDTO;
import web.todo.ToDoWeb.service.RequestService;

import javax.validation.Valid;

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
}
