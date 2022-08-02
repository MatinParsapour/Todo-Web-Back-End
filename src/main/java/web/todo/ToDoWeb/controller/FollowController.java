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
    public void followRequest(@RequestParam("applicantUsername") String applicantId, @RequestParam("responderUsername") String responderId) {
        followRequest.followRequest(applicantId, responderId);
    }

    @PutMapping("/change-follow-request-status")
    public void changeFollowRequestStatus(@RequestParam("status") FollowRequestStatus status, @RequestParam("requestId") String requestId) {
        followRequest.changeFollowRequestStatus(status, requestId);
    }

    @GetMapping("/get-all-user-requests/{responderUsername}")
    public List<FollowRequest> getAllUserFollowRequests(@PathVariable("responderUsername") String responderUsername) {
        return followRequest.getAllUserFollowRequests(responderUsername);
    }

    @GetMapping("/get-result-of-request/{responderUsername}/{applicantUsername}")
    public FollowRequestStatus getResultOfRequest(@PathVariable("responderUsername") String responderUsername, @PathVariable("applicantUsername") String applicantUsername) {
        return followRequest.getResultOfRequest(responderUsername, applicantUsername);
    }
}
