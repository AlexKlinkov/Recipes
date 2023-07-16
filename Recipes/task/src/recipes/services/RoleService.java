package recipes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.models.Role;
import recipes.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role saveRoleInBD(String role) {
        if (roleRepository.existsByName(role)) {
            return roleRepository.save(new Role(role));
        } else {
            return roleRepository.getByName(role);
        }
    }
}
