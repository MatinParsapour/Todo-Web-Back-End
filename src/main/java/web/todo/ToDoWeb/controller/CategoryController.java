package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.todo.ToDoWeb.model.ToDo;
import web.todo.ToDoWeb.service.CategoryFactory;

import java.util.Set;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryFactory categoryFactory;

    @Autowired
    public CategoryController(CategoryFactory categoryFactory) {
        this.categoryFactory = categoryFactory;
    }

    @GetMapping("/get-category-to-dos/{categoryName}/{userId}")
    public Set<ToDo> getCategoryToDos(@PathVariable("categoryName") String categoryName, @PathVariable("userId") String userId) {
        return categoryFactory.getToDosByCategory(categoryName, userId);
    }
}
