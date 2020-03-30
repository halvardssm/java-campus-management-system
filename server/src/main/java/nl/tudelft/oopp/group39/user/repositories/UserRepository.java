package nl.tudelft.oopp.group39.user.repositories;

import java.util.List;
import nl.tudelft.oopp.group39.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String userName);

    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT('%',:name,'%') AND u.role Like CONCAT('%',:role,'%')")
    List<User> filterUsers(@Param("name") String name, @Param("role") String role);
}
