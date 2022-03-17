package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.DoplicateException;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.ToDoFolder;
import web.todo.ToDoWeb.model.User;
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
    public void addFolder(String username, String folderName) {
        notEmptyAssertion(username);
        notEmptyAssertion(folderName);

        existByFolderNameAssertion(username, folderName, "The folder name is doplicate");

        if (userRepository.findByIdAndIsDeletedFalse(username).isPresent()) {
            User user = userRepository.findByIdAndIsDeletedFalse(username).get();
            ToDoFolder folder = new ToDoFolder();
            folder.setName(folderName);
            user.getToDoFolders().add(folder);
            save(user);
        } else {
            throw new NotFoundException("The user not found");
        }
    }



    @Override
    public Set<ToDoFolder> getUserFolders(String username) {
        if (userRepository.findByIdAndIsDeletedFalse(username).isPresent()) {
            User user = userRepository.findByIdAndIsDeletedFalse(username).get();
            return user.getToDoFolders();
        } else {
            throw new NotFoundException("No user found with the provided username");
        }
    }

    @Override
    public Set<ToDoFolder> getToDoFolder(String username, String toDoFolderName) {
        if (userRepository.findByUserNameAndToDoFoldersNameAndIsDeletedFalse(username, toDoFolderName).isPresent()) {
            return userRepository.findByUserNameAndToDoFoldersNameAndIsDeletedFalse(username, toDoFolderName).get().getToDoFolders();
        } else {
            throw new NotFoundException("The folder name you are looking for doesn't exists");
        }
    }

    @Override
    public void changeFolderName(String oldName, String newName, String username) {
        existByFolderNameAssertion(username, newName, "You have already a folder with the same name");

        if(userRepository.findByToDoFoldersNameAndUserNameAndIsDeletedFalse(oldName,username).isPresent()){
            User user = userRepository.findByToDoFoldersNameAndUserNameAndIsDeletedFalse(oldName, username).get();
            user.getToDoFolders().stream().filter( folder -> folder.getName().equals(oldName)).forEach( folder -> folder.setName(newName));
            save(user);
        } else {
            throw new NotFoundException("The username or folder name provided is wrong");
        }
    }

    @Override
    public void deleteFolder(String folderName, String username) {
        if(userRepository.findByToDoFoldersNameAndUserNameAndIsDeletedFalse(folderName,username).isPresent()){
            User user = userRepository.findByToDoFoldersNameAndUserNameAndIsDeletedFalse(folderName, username).get();
            user.getToDoFolders().removeIf(folder -> folder.getName().equals(folderName));
            save(user);
        } else {
            throw new NotFoundException("The username or folder name provided is wrong");
        }
    }

    private void existByFolderNameAssertion(String username, String folderName, String message) {
        if (existsByToDoFolderName(folderName, username)) {
            throw new DoplicateException(message);
        }
    }

    @Override
    public Boolean existsByToDoFolderName(String folderName, String username) {
        return userRepository.existsByToDoFoldersNameAndUserNameAndIsDeletedFalse(folderName, username);
    }

    private void notEmptyAssertion(String attribute){
        if (isNull(attribute) || isBlank(attribute) || isWhiteSpace(attribute) || isEmpty(attribute)) {
            throw new EmptyException("The password provided is empty");
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
