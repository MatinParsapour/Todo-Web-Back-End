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

    private String id;

    private String firstName;

    private String userName;

    private String email;

    private String password;

    private String birthDay;
}
