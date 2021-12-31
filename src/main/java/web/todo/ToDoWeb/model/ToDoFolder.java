package web.todo.ToDoWeb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToDoFolder {
    private static final String NAME = "name";
    private static final String TO_DO_FOLDER = "to_do_folder";
    private static final String TO_DO_LISTS = "to_do_lists";

    @Field(name = NAME)
    private String name;

    @Field(name = TO_DO_FOLDER)
    private Set<ToDoFolder> toDoFolders = new TreeSet<>();

    @Field(name = TO_DO_LISTS)
    private Set<ToDoList> toDoLists = new TreeSet<>();
}
