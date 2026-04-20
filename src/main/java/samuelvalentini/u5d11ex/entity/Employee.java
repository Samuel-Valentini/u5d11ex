package samuelvalentini.u5d11ex.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false, updatable = false)
    private Long employeeId;

    @NotBlank(message = "Field is required")
    @Size(max = 255, message = "Field must be at most 255 characters long")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Field is required")
    @Size(min = 8, max = 255, message = "Field must be between 8 and 255 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Field is required")
    @Size(max = 255, message = "Field must be at most 255 characters long")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Field is required")
    @Size(max = 255, message = "Field must be at most 255 characters long")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Field is required")
    @Email
    @Size(max = 255, message = "Field must be at most 255 characters long")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Field is required")
    @Size(max = 255, message = "Field must be at most 255 characters long")
    @URL
    @Column(name = "profile_picture", nullable = false)
    private String profilePicture;

    public Employee(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = "https://ui-avatars.com/api/?name=" + firstName + "+" + lastName;
    }

    protected Employee() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}

