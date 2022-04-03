package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.service.CategoryFactory;
import web.todo.ToDoWeb.service.UserService;

import java.util.Set;

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
     * @param userId include id
     * @return a list of categories
     */
    @GetMapping("/get-category-to-dos/{categoryName}/{userId}")
    public Set<ToDo> getCategoryToDos(@PathVariable("categoryName") String categoryName, @PathVariable("userId") String userId){
        return categoryFactory.getToDosByCategory(categoryName, userId);
    }
}
