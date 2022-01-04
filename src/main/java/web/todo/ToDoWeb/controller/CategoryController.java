package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.Category;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.UserDTO;
import web.todo.ToDoWeb.service.CategoryFactory;
import web.todo.ToDoWeb.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryFactory categoryFactory;
    private final UserService userService;

    @Autowired
    public CategoryController(CategoryFactory categoryFactory, UserService userService) {
        this.categoryFactory = categoryFactory;
        this.userService = userService;
    }

    /**
     * Get category name and user dto and find todos relate to
     * category name and user
     * @param categoryName name of category user looking for
     * @param userDTO include id
     * @return a list of categories
     */
    @GetMapping("/get-category-to-dos/{categoryName}")
    public List<Category> getCategoryToDos(@PathVariable("categoryName") String categoryName,@RequestBody UserDTO userDTO){
        Optional<User> user = userService.findById(userDTO.getId());
        return categoryFactory.getToDosByCategory(categoryName, user.get());
    }
}
