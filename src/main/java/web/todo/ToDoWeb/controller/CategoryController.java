package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.todo.ToDoWeb.service.impl.CategoryFactory;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryFactory categoryFactory;

    @Autowired
    public CategoryController(CategoryFactory categoryFactory) {
        this.categoryFactory = categoryFactory;
    }
}
