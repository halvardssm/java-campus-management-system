package nl.tudelft.oopp.group39.user.repositories;

import nl.tudelft.oopp.group39.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String userName);
}
