package web.todo.ToDoWeb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    @NotNull(message = "You should enter your username")
    @NotBlank(message = "You should enter your username")
    @NotEmpty(message = "You should enter your username")
    private String userName;

    @NotNull(message = "You should enter password")
    @NotBlank(message = "You should enter password")
    @NotEmpty(message = "You should enter password")
    @Pattern(regexp = "^(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[0-9])+)(?=(.*[!@#$%^&*()\\-__+.])+).{8,}$", message = "Please choose a stronger password")
    private String password;
}
