package web.todo.ToDoWeb.controller;

import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.service.SettingsService;

@RestController
@RequestMapping("/settings")
public class SettingsController {
    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }


    @GetMapping("/{userName}/personal-info")
    public UserDTO personal(@PathVariable("userName") String userName) {
        return settingsService.getUserPersonalInfo(userName);
    }

    @GetMapping("/{userName}/security-info")
    public UserDTO security(@PathVariable("userName") String userName) {
        return settingsService.getUserSecurityInfo(userName);
    }

    @GetMapping("/{userName}/account-info")
    public UserDTO account(@PathVariable("userName") String userName) {
        return settingsService.getUserAccountInfo(userName);
    }

    @PutMapping("/update/personal-info")
    public void updatePersonalInfo(@RequestBody UserDTO userDTO) {
        settingsService.updatePersonalInfo(userDTO);
    }

    @PutMapping("/update/security-info")
    public void updateSecurityInfo(@RequestBody UserDTO userDTO) {
        settingsService.updateSecurityInfo(userDTO);
    }

    @PutMapping("/update/account-info")
    public void updateAccountInfo(@RequestBody UserDTO userDTO) {
        settingsService.updateAccountInfo(userDTO);
    }
}
