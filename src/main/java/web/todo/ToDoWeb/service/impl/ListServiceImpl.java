package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import web.todo.ToDoWeb.exception.DoplicateException;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.model.ToDoList;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.FilledValidation;
import web.todo.ToDoWeb.service.ListService;

@Service
public class ListServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements ListService, FilledValidation {

    private final UserRepository userRepository;

    @Autowired
    public ListServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    public User addListToFolder(String folderName, String listName, String username) {
        if (isEmpty(folderName) || isEmpty(listName) || isEmpty(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isNull(folderName) || isNull(listName) || isNull(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isBlank(folderName) || isBlank(listName) || isBlank(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isWhiteSpace(folderName) || isWhiteSpace(listName) || isWhiteSpace(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (existsByToDoListName(listName, folderName, username)){
            throw new DoplicateException("The list with the same name already exists");
        }
        if (userRepository.findByToDoFoldersNameAndUserName(folderName, username).isPresent()){
            User user = userRepository.findByToDoFoldersNameAndUserName(folderName, username).get();
            ToDoList toDoList = new ToDoList();
            toDoList.setName(listName);
            user.getToDoFolders().stream().filter(folder -> folder.getName().equals(folderName)).forEach(folder -> folder.getToDoLists().add(toDoList));
            return save(user);
        }else {
            throw new NotFoundException("The username or folder name provided is wrong");
        }
    }

    @Override
    public Boolean existsByToDoListName(String toDoListName, String toDoFolderName, String username) {
        return userRepository.existsByToDoFoldersToDoListsNameAndToDoFoldersNameAndUserName(toDoListName, toDoFolderName, username) != null;
    }

    @Override
    public void changeListName(String oldListName, String newListName, String folderName, String username) {
        if (isEmpty(folderName) || isEmpty(oldListName) || isEmpty(newListName) || isEmpty(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isNull(folderName) || isNull(oldListName) || isNull(newListName) || isNull(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isBlank(folderName) || isBlank(oldListName) || isBlank(newListName) || isBlank(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isWhiteSpace(folderName) || isWhiteSpace(oldListName) || isWhiteSpace(newListName) || isWhiteSpace(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (!existsByToDoListName(oldListName, folderName, username)){
            throw new EmptyException("The list name provided doesn't belong to user");
        }
        if (existsByToDoListName(newListName, folderName, username)){
            throw new DoplicateException("The list with the same name already exists");
        }
        if (userRepository.findByToDoFoldersNameAndUserName(folderName, username).isPresent()){
            User user = userRepository.findByToDoFoldersNameAndUserName(folderName, username).get();
            user.getToDoFolders().stream().filter(folder -> folder.getName().equals(folderName)).forEach( folder -> folder.getToDoLists().stream().filter(list -> list.getName().equals(oldListName)).forEach(list -> list.setName(newListName)));
            save(user);
        }else {
            throw new NotFoundException("The folder name or username is wrong");
        }
    }

    @Override
    public void deleteList(String listName, String folderName, String username) {
        if (isEmpty(folderName) || isEmpty(listName) || isEmpty(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isNull(folderName) || isNull(listName) || isNull(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isBlank(folderName) || isBlank(listName) || isBlank(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isWhiteSpace(folderName) || isWhiteSpace(listName) || isWhiteSpace(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (!existsByToDoListName(listName, folderName, username)){
            throw new EmptyException("The list name provided doesn't belong to user");
        }
        if (userRepository.findByToDoFoldersNameAndUserName(folderName, username).isPresent()){
            User user = userRepository.findByToDoFoldersNameAndUserName(folderName, username).get();
            user.getToDoFolders().stream().filter(folder -> folder.getName().equals(folderName)).forEach(folder -> folder.getToDoLists().removeIf(toDoList -> toDoList.getName().equals(listName)));
            save(user);
        } else {
            throw new NotFoundException("The folder name or username is wrong");
        }
    }

    @Override
    public void insertToDoToList(ToDo toDo, String listName, String folderName, String username) {
        if (isEmpty(folderName) || isEmpty(listName) || isEmpty(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isNull(folderName) || isNull(listName) || isNull(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isBlank(folderName) || isBlank(listName) || isBlank(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isWhiteSpace(folderName) || isWhiteSpace(listName) || isWhiteSpace(username)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (!existsByToDoListName(listName, folderName, username)){
            throw new EmptyException("The list name provided doesn't belong to user");
        }
        if (userRepository.findByToDoFoldersNameAndUserName(folderName, username).isPresent()){
            User user = userRepository.findByToDoFoldersNameAndUserName(folderName, username).get();
            user.getToDoFolders().stream().filter(folder -> folder.getName().equals(folderName)).forEach(folder -> folder.getToDoLists().stream().filter(toDoList -> toDoList.getName().equals(listName)).forEach(list -> list.getToDos().add(toDo)));
            save(user);
        } else {
            throw new NotFoundException("The folder name or username is wrong");
        }
    }

    @Override
    public void removeToDoFromList(String folderName, String listName, String userName, String toDoId) {
        if (isEmpty(folderName) || isEmpty(listName) || isEmpty(userName)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isNull(folderName) || isNull(listName) || isNull(userName)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isBlank(folderName) || isBlank(listName) || isBlank(userName)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (isWhiteSpace(folderName) || isWhiteSpace(listName) || isWhiteSpace(userName)) {
            throw new EmptyException("Check form one or more fields are empty");
        }
        if (!existsByToDoListName(listName, folderName, userName)){
            throw new EmptyException("The list name provided doesn't belong to user");
        }
        if (userRepository.findByToDoFoldersNameAndUserName(folderName, userName).isPresent()){
            User user = userRepository.findByToDoFoldersNameAndUserName(folderName, userName).get();
            user.getToDoFolders()
                    .stream()
                    .filter(folder -> folder.getName().equals(folderName))
                    .forEach(folder -> folder.getToDoLists()
                            .stream()
                            .filter(toDoList -> toDoList.getName().equals(listName))
                            .forEach(toDoList -> toDoList.getToDos()
                                    .removeIf(toDo -> toDo.getId().equals(toDoId))));
            save(user);
        } else {
            throw new NotFoundException("The folder name or username is wrong");
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
