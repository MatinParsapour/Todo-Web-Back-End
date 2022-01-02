package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.DoplicateException;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.FilledValidation;
import web.todo.ToDoWeb.service.FolderService;

import java.util.Set;

@Service
public class FolderServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements FolderService, FilledValidation {

    private final UserRepository userRepository;

    public FolderServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    public void addFolder(UserDTO userDTO, String folderName) {
        if (isEmpty(folderName) || isNull(folderName) || isWhiteSpace(folderName) || isBlank(folderName)) {
            throw new EmptyException("The folder must be declared");
        }
        if (isEmpty(userDTO.getUserName()) || isNull(userDTO.getUserName()) || isWhiteSpace(userDTO.getUserName()) || isBlank(userDTO.getUserName())) {
            throw new EmptyException("The username must be declared");
        }
        if (existsByToDoFolderName(folderName, userDTO.getUserName())) {
            throw new DoplicateException("The folder name is doplicate");
        }
        if (findById(userDTO.getId()).isPresent()) {
            User user = findById(userDTO.getId()).get();
            ToDoFolder folder = new ToDoFolder();
            folder.setName(folderName);
            user.getToDoFolders().add(folder);
            save(user);
        } else {
            throw new NotFoundException("The user not found");
        }
    }

    @Override
    public Boolean existsByToDoFolderName(String folderName, String username) {
        return userRepository.existsByToDoFoldersNameAndUserName(folderName, username);
    }

    @Override
    public Set<ToDoFolder> getUserFolders(String username) {
        if (userRepository.findByUserName(username).isPresent()) {
            User user = userRepository.findByUserName(username).get();
            return user.getToDoFolders();
        } else {
            throw new NotFoundException("No user found with the provided username");
        }
    }

    @Override
    public Set<ToDoFolder> getToDoFolder(String username, String toDoFolderName) {
        if (userRepository.findByUserNameAndToDoFoldersName(username, toDoFolderName).isPresent()) {
            return userRepository.findByUserNameAndToDoFoldersName(username, toDoFolderName).get().getToDoFolders();
        } else {
            throw new NotFoundException("The folder name you are looking for doesn't exists");
        }
    }

    @Override
    public void changeFolderName(String oldName, String newName, String username) {
        if(existsByToDoFolderName(newName, username)){
            throw new DoplicateException("You have already a folder with the same name");
        }
        if(userRepository.findByToDoFoldersNameAndUserName(oldName,username).isPresent()){
            User user = userRepository.findByToDoFoldersNameAndUserName(oldName, username).get();
            user.getToDoFolders().stream().filter( folder -> folder.getName().equals(oldName)).forEach( folder -> folder.setName(newName));
            save(user);
        } else {
            throw new NotFoundException("The username or folder name provided is wrong");
        }
    }

    @Override
    public Boolean isEmpty(String field) {
        return field.isEmpty();
    }

    @Override
    public Boolean isBlank(String field) {
        return field.isBlank();
    }

    @Override
    public Boolean isNull(String field) {
        return field == null;
    }

    @Override
    public Boolean isWhiteSpace(String field) {
        return field.trim().isEmpty();
    }
}
