package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import web.todo.ToDoWeb.model.Tag;
import web.todo.ToDoWeb.model.dto.TagDTO;

import java.util.List;

public interface TagRepository extends MongoRepository<Tag, String> {

    boolean existsByName(String name);

    Tag findByName(String name);

    @Query("{ 'name' : { '$regex' : ?0 , $options: 'i'}}")
    List<Tag> findByNameContains(String name);

    TagDTO getByName(String tagName);
}
