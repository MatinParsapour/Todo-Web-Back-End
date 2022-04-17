package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.enumeration.FollowRequestStatus;
import web.todo.ToDoWeb.model.FollowRequest;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.FollowRequestRepository;
import web.todo.ToDoWeb.service.FollowRequestService;
import web.todo.ToDoWeb.service.UserService;

import java.util.List;

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

    @Override
    public void changeFollowRequestStatus(FollowRequestStatus status, String requestId) {
        FollowRequest followRequest = findById(requestId).get();
        followRequest.setStatus(status);
        save(followRequest);
        if (status == FollowRequestStatus.ACCEPTED){
            updateUser(followRequest);
        }
    }

    private void updateUser(FollowRequest followRequest) {
        userService.addToFollowers(followRequest.getResponder(), followRequest.getApplicant());
        userService.addToFollowings(followRequest.getResponder(), followRequest.getApplicant());
    }

    @Override
    public List<FollowRequest> getAllUserFollowRequests(String responderId) {
        List<FollowRequest> followRequests = followRequestRepository
                .findAllByResponderAndStatus(
                        userService.findById(responderId).get(),
                        FollowRequestStatus.UNSPECIFIED);
        saveImportantPropertiesInList(followRequests);
        return followRequests;
    }

    private void saveImportantPropertiesInList(List<FollowRequest> followRequests) {
        followRequests.forEach(followRequest -> {
            removeUserImportantProperties(followRequest.getApplicant());
            removeUserImportantProperties(followRequest.getResponder());
        });
    }

    private void removeUserImportantProperties(User user){
        user.setLastLoginDate(null);
        user.setAuthorities(null);
        user.setIsDeleted(null);
        user.setToDoFolders(null);
        user.setAge(0);
        user.setBirthDay(null);
        user.setPhoneNumber(null);
        user.setValidatorEmail(null);
        user.setPassword(null);
        user.setRole(null);
        user.setToDos(null);
        user.setRegisterDate(null);
        user.setFollowings(null);
        user.setFollowers(null);
        user.setIsBlocked(null);
    }
}
