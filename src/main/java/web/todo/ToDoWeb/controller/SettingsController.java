package web.todo.ToDoWeb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
