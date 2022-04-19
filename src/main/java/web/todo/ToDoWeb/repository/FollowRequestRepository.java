package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.enumeration.FollowRequestStatus;
import web.todo.ToDoWeb.model.FollowRequest;
import web.todo.ToDoWeb.model.User;

import java.util.List;

public interface FollowRequestRepository extends MongoRepository<FollowRequest, String> {

    List<FollowRequest> findAllByResponderAndStatus(User responderId, FollowRequestStatus status);

    FollowRequest findByApplicantAndResponderAndStatus(User applicant, User responder, FollowRequestStatus status);
}
