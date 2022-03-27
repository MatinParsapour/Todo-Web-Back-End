package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.Request;
import web.todo.ToDoWeb.model.dto.RequestDTO;

public interface RequestService extends BaseService<Request, String> {

    void startNewRequest(RequestDTO requestDTO);
}
