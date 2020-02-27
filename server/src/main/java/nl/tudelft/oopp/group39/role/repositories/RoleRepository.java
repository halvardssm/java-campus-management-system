package nl.tudelft.oopp.group39.role.repositories;

import nl.tudelft.oopp.group39.role.entities.Role;
import nl.tudelft.oopp.group39.role.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(Roles role);
}
