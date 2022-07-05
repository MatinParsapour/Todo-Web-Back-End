package web.todo.ToDoWeb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.todo.ToDoWeb.enumeration.Provider;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDTO {

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

    @NotNull(message = "You should enter password")
    @NotBlank(message = "You should enter password")
    @NotEmpty(message = "You should enter password")
    @Pattern(regexp = "^(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[0-9])+)(?=(.*[!@#$%^&*()\\-__+.])+).{8,}$", message = "Please choose a stronger password")
    private String password;

    @NotNull(message = "You should provide Provider")
    @NotBlank(message = "You should provide Provider")
    @NotEmpty(message = "You should provide Provider")
    private Provider provider;
}
