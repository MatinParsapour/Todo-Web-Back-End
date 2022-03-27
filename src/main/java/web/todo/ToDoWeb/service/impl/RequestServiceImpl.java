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
}
