package nl.tudelft.oopp.role.repositories;

import nl.tudelft.oopp.role.entities.Role;
import nl.tudelft.oopp.role.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(Roles role);
}
