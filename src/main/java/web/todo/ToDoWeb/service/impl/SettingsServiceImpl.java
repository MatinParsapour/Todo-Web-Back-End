package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.SettingsService;

@Service
public class SettingsServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements SettingsService {
    private final UserRepository userRepository;

    public SettingsServiceImpl(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }

    @Override
    public UserDTO getUserPersonalInfo(String userId) {
        return userRepository.getUserPersonalInfo(userId).orElseThrow(() -> new NotFoundException("No user found"));
    }

    @Override
    public UserDTO getUserSecurityInfo(String userId) {
        return userRepository.getUserSecurityInfo(userId).orElseThrow(() -> new NotFoundException("No user found"));
    }

    @Override
    public UserDTO getUserAccountInfo(String userId) {
        return userRepository.getUserAccountInfo(userId).orElseThrow(() -> new NotFoundException("No user found"));
    }

    @Override
    public void updateAccountInfo(UserDTO userDTO) {
        User user = findById(userDTO.getId()).orElseThrow(() -> new NotFoundException("No user found"));
        user.setAccessLevel(userDTO.getAccessLevel());
    }

    @Override
    public void updateSecurityInfo(UserDTO userDTO) {
        User user = findById(userDTO.getId()).orElseThrow(() -> new NotFoundException("No user found"));
        user.setUserName(userDTO.getUserName());
    }

    @Override
    public void updatePersonalInfo(UserDTO userDTO) {
        User user = findById(userDTO.getId()).orElseThrow(() -> new NotFoundException("No user found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setBirthDay(userDTO.getBirthDay());
        user.setAge(userDTO.getAge());
        save(user);
    }
}
