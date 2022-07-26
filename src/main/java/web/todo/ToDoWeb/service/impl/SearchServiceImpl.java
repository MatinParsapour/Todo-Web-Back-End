package web.todo.ToDoWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.service.SearchService;
import web.todo.ToDoWeb.service.TagService;
import web.todo.ToDoWeb.service.UserService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final TagService tagService;

    private final UserService userService;

    @Autowired
    public SearchServiceImpl(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    @Override
    public List<Object> search(String keyword) {
        List<Object> objects = new LinkedList<>();
        List<Tag> tags = tagService.search(keyword);
        tags = tags.stream().peek(tag -> {
            tag.setToDos(null);
            tag.setCreatedBy(null);
        }).collect(Collectors.toList());
        List<User> users = userService.search(keyword);
        users = users.stream().peek(this::removeUserCrucialInfo).collect(Collectors.toList());
        objects.addAll(tags);
        objects.addAll(users);
        return objects;
    }

    private void removeUserCrucialInfo(User user) {
        user.setLastLoginDate(null);
        user.setAuthorities(null);
        user.setIsDeleted(null);
        user.setToDoFolders(null);
        user.setAge(0);
        user.setBirthDay(null);
        user.setPhoneNumber(null);
        user.setPassword(null);
        user.setRole(null);
        user.setToDos(null);
        user.setRegisterDate(null);
        user.setFollowings(null);
        user.setFollowers(null);
        user.setIsBlocked(null);
    }
}
