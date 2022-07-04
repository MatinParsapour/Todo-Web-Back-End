package web.todo.ToDoWeb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.todo.ToDoWeb.enumeration.AccessLevel;
import web.todo.ToDoWeb.enumeration.Role;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.util.AES;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    private String userName;

    @NotNull(message = "You should enter email")
    @NotBlank(message = "You should enter email")
    @NotEmpty(message = "You should enter email")
    @Email(message = "This is not valid structure for an email")
    private String email;

    private Long phoneNumber;

    private String password;

    private String birthDay;

    private String profileImageUrl;

    private transient int age;

    private Role role;

    private String[] authorities;

    private Boolean isDeleted = Boolean.FALSE;

    private Boolean isBlocked = Boolean.FALSE;

    private Date registerDate;

    private Date lastLoginDate;

    @Size(max = 300)
    private String bio;

    private Set<User> followers = new HashSet<>();

    private Set<User> followings = new HashSet<>();

    private AccessLevel accessLevel;

    public Integer getAge() {
        if (birthDay != null) {
            LocalDate birthday = LocalDate.parse(birthDay);
            LocalDate now = LocalDate.now();
            return birthday.until(now).getYears();
        }
        return null;
    }

    public String getPassword() throws Exception {
        if (password != null) {
            return AES.decrypt(password);
        }
        return null;
    }
}
