package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.FollowRequestStatus;
import web.todo.ToDoWeb.model.FollowRequest;
import web.todo.ToDoWeb.repository.FollowRequestRepository;
import web.todo.ToDoWeb.service.FollowRequestService;
import web.todo.ToDoWeb.service.UserService;

@Service
public class FollowRequestServiceImpl extends BaseServiceImpl<FollowRequest, String, FollowRequestRepository> implements FollowRequestService {

    private final FollowRequestRepository followRequestRepository;
    private final UserService userService;

    @Autowired
    public FollowRequestServiceImpl(FollowRequestRepository repository, UserService userService) {
        super(repository);
        this.followRequestRepository = repository;
        this.userService = userService;
    }

    @Override
    public void followRequest(String applicantId, String responderId) {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setResponder(userService.getUserById(responderId));
        followRequest.setApplicant(userService.getUserById(applicantId));
        save(followRequest);
    }
}
