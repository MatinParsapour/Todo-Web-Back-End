package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.service.PhoneService;

@RestController
@RequestMapping("/phone")
public class PhoneController {

    private final PhoneService phoneService;

    @Autowired
    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PutMapping("/update-phone-number")
    public void updatePhoneNumber(@RequestParam("phoneNumber") Long phoneNumber, @RequestParam("username") String username){
        phoneService.validatePhoneNumberAndUsername(phoneNumber, username);
    }

    @GetMapping("/check-code/{code}")
    public Boolean checkPhoneNumberCode(@PathVariable("code") int code){
        return phoneService.validateCode(code);
    }

    @GetMapping("/resend-code")
    public void resendCode(){
        phoneService.resendCode();
    }
}
