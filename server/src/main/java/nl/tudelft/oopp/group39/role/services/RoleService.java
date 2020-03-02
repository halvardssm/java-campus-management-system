package nl.tudelft.oopp.group39.role.services;

import java.util.List;
import nl.tudelft.oopp.group39.role.entities.Role;
import nl.tudelft.oopp.group39.role.enums.Roles;
import nl.tudelft.oopp.group39.role.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public RoleService() {
    }

    public Role readRole(Roles role) {
        return roleRepository.findByRole(role);
    }

    public Role readRole(String role) {
        return roleRepository.findByRole(Roles.valueOf(role));
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
}
