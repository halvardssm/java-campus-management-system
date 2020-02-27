package nl.tudelft.oopp.group39.role.service;

import java.util.List;
import nl.tudelft.oopp.group39.role.entity.Role;
import nl.tudelft.oopp.group39.role.enums.Roles;
import nl.tudelft.oopp.group39.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public RoleService() {
    }

    public Role readRole(Roles role) {
        return roleRepository.findByRole(role.name());
    }

    public Role readRole(String role) {
        return roleRepository.findByRole(role);
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    /**
     * Initiates the db with all the roles.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initRoles() {
        if (roleRepository.count() > 0) {
            return;
        }

        for (Roles role : Roles.values()) {
            roleRepository.save(new Role(role));
        }

        roleRepository.flush();

        System.out.println("[SEED] Roles created");
    }
}
