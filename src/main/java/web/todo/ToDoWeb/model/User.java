package web.todo.ToDoWeb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import web.todo.ToDoWeb.util.AES;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Document(collection = User.TABLE_NAME)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    public static final String TABLE_NAME = "user_table";

    @Id
    private String id;

    private String firstName;

    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String validatorEmail;

    private String password;

    @Indexed(unique = true)
    private Long phoneNumber;

    private String birthDay;

    private String profileImageUrl;

    @Transient
    private transient int age;

    private Boolean isDeleted = Boolean.FALSE;

    private Set<ToDoFolder> toDoFolders = new TreeSet<>();

    public String getPassword() throws Exception {
        if (password != null) {
            return AES.decrypt(password);
        }
        return null;
    }

    public Integer getAge() {
        if (birthDay != null) {
            LocalDate birthday = LocalDate.parse(birthDay);
            LocalDate now = LocalDate.now();
            return birthday.until(now).getYears();
        }
        return null;
    }
}
