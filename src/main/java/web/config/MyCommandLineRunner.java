package web.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import web.model.AppUser;
import web.model.Role;
import web.repository.RoleRepository;
import web.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public MyCommandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        if (userRepository.findByUsername("test1").isPresent()) {
            AppUser user = new AppUser();
            user.setUsername("Test");
            user.setUsersurname("Surname");
            user.setAge(20);
            user.setEmail("test1@mail.com");
            user.setPassword(passwordEncoder.encode("test1"));

            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("test2").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("Admin1");
            admin.setUsersurname("Admin");
            admin.setAge(21);
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("admin"));

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
        }
    }

}
