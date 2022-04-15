package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.todo.ToDoWeb.service.FollowRequestService;

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
}
