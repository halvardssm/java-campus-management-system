package nl.tudelft.oopp.group39.user.repository;

import nl.tudelft.oopp.group39.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String userName);
}
