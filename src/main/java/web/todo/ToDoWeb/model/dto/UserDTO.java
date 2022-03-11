package web.todo.ToDoWeb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;

    @NotNull(message = "You should enter first name")
    @NotBlank(message = "You should enter first name")
    @NotEmpty(message = "You should enter first name")
    private String firstName;

    @NotNull(message = "You should enter last name")
    @NotBlank(message = "You should enter last name")
    @NotEmpty(message = "You should enter last name")
    private String lastName;

    @NotNull(message = "You should enter username")
    @NotBlank(message = "You should enter username")
    @NotEmpty(message = "You should enter username")
    private String userName;

    @NotNull(message = "You should enter email")
    @NotBlank(message = "You should enter email")
    @NotEmpty(message = "You should enter email")
    @Email(message = "This is not valid structure for an email")
    private String email;

    private Long phoneNumber;

    private String birthDay;

    private String profileImageUrl;

    private transient int age;

    public Integer getAge() {
        if (birthDay != null) {
            LocalDate birthday = LocalDate.parse(birthDay);
            LocalDate now = LocalDate.now();
            return birthday.until(now).getYears();
        }
        return null;
    }
}
