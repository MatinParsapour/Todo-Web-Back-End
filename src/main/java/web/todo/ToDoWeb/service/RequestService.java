package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.model.dto.RequestDTO;

import java.util.List;

public interface RequestService extends BaseService<Request, String> {

    void startNewRequest(RequestDTO requestDTO);

    List<Request> getUserRequests(String userId);

    List<Request> getAllUsersRequests();
}
