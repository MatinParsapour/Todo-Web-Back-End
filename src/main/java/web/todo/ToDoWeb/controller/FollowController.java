package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.enumeration.FollowRequestStatus;
import web.todo.ToDoWeb.model.FollowRequest;
import web.todo.ToDoWeb.service.FollowRequestService;

import java.util.List;

@RestController
@RequestMapping("/follow-request")
public class FollowController {

    private final FollowRequestService followRequest;

    @Autowired
    public FollowController(FollowRequestService followRequest) {
        this.followRequest = followRequest;
    }

    @PostMapping("/send-follow-request")
    public void followRequest(@RequestParam("applicantId") String applicantId, @RequestParam("responderId") String responderId){
        followRequest.followRequest(applicantId, responderId);
    }

    @PutMapping("/change-follow-request-status")
    public void changeFollowRequestStatus(@RequestParam("status")FollowRequestStatus status, @RequestParam("requestId") String requestId){
        followRequest.changeFollowRequestStatus(status, requestId);
    }

    @GetMapping("/get-all-user-requests/{responderId}")
    public List<FollowRequest> getAllUserFollowRequests(@PathVariable("responderId") String responderId){
        return followRequest.getAllUserFollowRequests(responderId);
    }
}
