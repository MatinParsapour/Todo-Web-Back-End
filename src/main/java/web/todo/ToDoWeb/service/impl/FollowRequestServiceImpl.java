package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.FollowRequest;
import web.todo.ToDoWeb.repository.FollowRequestRepository;
import web.todo.ToDoWeb.service.FollowRequestService;

@Service
public class FollowRequestServiceImpl extends BaseServiceImpl<FollowRequest, String, FollowRequestRepository> implements FollowRequestService {

    public FollowRequestServiceImpl(FollowRequestRepository repository) {
        super(repository);
    }
}
