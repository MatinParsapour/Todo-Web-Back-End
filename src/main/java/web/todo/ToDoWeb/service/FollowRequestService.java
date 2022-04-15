package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.enumeration.FollowRequestStatus;
import web.todo.ToDoWeb.model.FollowRequest;

public interface FollowRequestService extends BaseService<FollowRequest, String> {

    void followRequest(String applicantId, String responderId);

    void changeFollowRequestStatus(FollowRequestStatus status, String requestId);
}
