package web.todo.ToDoWeb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.todo.ToDoWeb.util.AES;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private Long phoneNumber;

    private String birthDay;

    private transient int age;

    public int getAge() {
        LocalDate birthday = LocalDate.parse(birthDay);
        LocalDate now = LocalDate.now();
        return birthday.until(now).getYears();
    }
}
