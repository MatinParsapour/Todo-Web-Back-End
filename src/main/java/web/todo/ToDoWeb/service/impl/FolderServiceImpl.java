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
    public void addFolder(String userid, String folderName) {
        notEmptyAssertion(userid);
        notEmptyAssertion(folderName);

        existByFolderNameAssertion(userid, folderName, "The folder name is doplicate");

        User user = userRepository.findByUserNameAndIsDeletedFalse(userid)
                .orElseThrow(() -> new NotFoundException("The user not found"));
        ToDoFolder folder = new ToDoFolder();
        folder.setName(folderName);
        user.getToDoFolders().add(folder);
        save(user);
    }



    @Override
    public Set<ToDoFolder> getUserFolders(String userId) {
        User user = userRepository.findByUserNameAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException("No user found with the provided username"));
        return user.getToDoFolders();
    }

    @Override
    public Set<ToDoFolder> getToDoFolder(String userId, String toDoFolderName) {
        return userRepository.findByUserNameAndToDoFoldersNameAndIsDeletedFalse(userId, toDoFolderName)
                .orElseThrow(() -> new NotFoundException("The folder name you are looking for doesn't exists"))
                .getToDoFolders();
    }

    @Override
    public void changeFolderName(String oldName, String newName, String userId) {
        existByFolderNameAssertion(userId, newName, "You already have a folder with the same name");

        User user = userRepository.findByToDoFoldersNameAndIdAndIsDeletedFalse(oldName, userId)
                .orElseThrow(() -> new NotFoundException("The username or folder name provided is wrong"));

        user.getToDoFolders().stream()
                .filter( folder -> folder.getName().equals(oldName))
                .forEach( folder -> folder.setName(newName));

        save(user);
    }

    @Override
    public void deleteFolder(String folderName, String userId) {
        User user = userRepository.findByToDoFoldersNameAndIdAndIsDeletedFalse(folderName, userId)
                .orElseThrow(() -> new NotFoundException("The username or folder name provided is wrong"));

        user.getToDoFolders().removeIf(folder -> folder.getName().equals(folderName));

        save(user);
    }

    private void existByFolderNameAssertion(String username, String folderName, String message) {
        if (existsByToDoFolderName(folderName, username)) {
            throw new DoplicateException(message);
        }
    }

    @Override
    public Boolean existsByToDoFolderName(String folderName, String userId) {
        return userRepository.existsByToDoFoldersNameAndIdAndIsDeletedFalse(folderName, userId);
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
