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


    @GetMapping("/{userId}/personal-info")
    public UserDTO personal(@PathVariable("userId") String userId) {
        return settingsService.getUserPersonalInfo(userId);
    }

    @GetMapping("/{userId}/security-info")
    public UserDTO security(@PathVariable("userId") String userId) {
        return settingsService.getUserSecurityInfo(userId);
    }

    @GetMapping("/{userId}/account-info")
    public UserDTO account(@PathVariable("userId") String userId) {
        return settingsService.getUserAccountInfo(userId);
    }

    @PutMapping("/update/personal-info")
    public void updatePersonalInfo(@RequestBody UserDTO userDTO) {
        settingsService.updatePersonalInfo(userDTO);
    }

    @PutMapping("/update/security-info")
    public void updateSecurityInfo(@RequestBody UserDTO userDTO) {
        settingsService.updateSecurityInfo(userDTO);
    }
}
