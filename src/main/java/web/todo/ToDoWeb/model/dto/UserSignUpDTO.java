package web.todo.ToDoWeb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDTO {

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private String password;
}
