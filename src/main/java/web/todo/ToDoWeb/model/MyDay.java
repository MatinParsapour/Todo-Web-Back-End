package web.todo.ToDoWeb.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Document(collection = "MyDay")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyDay implements Category{

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Set<ToDo> toDos = new HashSet<>();
}
