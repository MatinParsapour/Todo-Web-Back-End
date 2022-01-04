package web.todo.ToDoWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.todo.ToDoWeb.service.impl.CategoryFactoryImpl;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryFactoryImpl categoryFactoryImpl;

    @Autowired
    public CategoryController(CategoryFactoryImpl categoryFactoryImpl) {
        this.categoryFactoryImpl = categoryFactoryImpl;
    }
}
