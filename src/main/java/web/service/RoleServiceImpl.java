package web.service;

import org.springframework.stereotype.Service;
import web.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean findByName(String name) {
        return roleRepository.findByName(name).isPresent();
    }


}
