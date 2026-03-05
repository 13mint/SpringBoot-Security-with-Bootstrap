package web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+$",
            message = "Name must contain oly letters"
    )
    private String username;
    @NotBlank
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+$",
            message = "Name must contain oly letters"
    )
    private String usersurname;

    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 120, message = "Age must be less than 120")
    private int age;

    @NotBlank
    @Email
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))

    private Set<Role> roles = new HashSet<>();

    public AppUser(Set<Role> roles) {
        this.roles = roles;
    }

    public AppUser(String username, String usersurname, int age, String email, String password, Set<Role> roles) {
        this.username = username;
        this.usersurname = usersurname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public AppUser() {

    }

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsersurname() {
        return usersurname;
    }

    public void setUsersurname(String usersurname) {
        this.usersurname = usersurname;
    }

    public int getAge() {return age;}

    public void setAge(int age) {this.age = age;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {return password;}

    public void setPassword(String password) { this.password = password;}

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) { this.roles = roles;}
}
