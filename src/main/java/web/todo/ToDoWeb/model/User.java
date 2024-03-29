package web.todo.ToDoWeb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import web.todo.ToDoWeb.enumeration.AccessLevel;
import web.todo.ToDoWeb.enumeration.Provider;
import web.todo.ToDoWeb.enumeration.Role;
import web.todo.ToDoWeb.util.AES;

import java.time.LocalDate;
import java.util.*;

@Document(collection = User.TABLE_NAME)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Comparable<User> {
    public static final String TABLE_NAME = "user_table";

    @Id
    private String id;

    private String firstName;

    private String userName;

    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String password;

    @Indexed(unique = true)
    private Long phoneNumber;

    private String birthDay;

    private String profileImageUrl;

    private AccessLevel accessLevel = AccessLevel.PRIVATE;

    @Transient
    private transient int age;

    private Boolean isDeleted = Boolean.FALSE;

    private boolean isPhoneVisible = false;

    private boolean isEmailVisible = false;

    @DBRef
    private Set<ToDo> toDos = new HashSet<>();

    @DBRef
    private Set<ToDo> savedToDos = new HashSet<>();

    private Set<ToDoFolder> toDoFolders = new TreeSet<>();

    private Role role;

    private String[] authorities;

    private Boolean isBlocked = Boolean.FALSE;

    private Date registerDate;

    private String bio;

    private Provider provider;

    private Date lastLoginDate;

    @DBRef
    private Set<User> followers = new HashSet<>();

    @DBRef
    private Set<User> followings = new HashSet<>();

    @DBRef
    private List<Tag> tags = new LinkedList<>();

    public String getPassword() throws Exception {
        if (password != null) {
            return AES.decrypt(password);
        }
        return null;
    }

    public Integer getAge() {
        if (birthDay == null) {
            return null;
        }
        LocalDate birthday = LocalDate.parse(birthDay);
        LocalDate now = LocalDate.now();
        return birthday.until(now).getYears();
    }

    @Override
    public int compareTo(User o) {
        return this.id.compareTo(o.id);
    }
}
