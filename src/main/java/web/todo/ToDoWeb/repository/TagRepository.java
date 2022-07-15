package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {

    boolean existsByName(String name);
}
