package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.FollowRequest;

public interface FollowRequestRepository extends MongoRepository<FollowRequest, String> {
}
