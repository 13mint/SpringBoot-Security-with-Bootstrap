package web.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.repository.RoleRepository;
import web.repository.UserRepository;
import org.springframework.stereotype.Service;
import web.model.AppUser;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {

        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(AppUser user) {
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow();
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(roleUser));
        }

        if(repo.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        if(repo.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        repo.save(user);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<AppUser> findAll() {
        return repo.findAll();
    }

    @Override
    public boolean findByUsername(String username) {
        return repo.findByUsername(username).isPresent();
    }

    @Override
    public boolean findByEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }


}
