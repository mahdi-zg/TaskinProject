package devteam.tn.taskin.Repository;

import devteam.tn.taskin.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByCin(String cin);
    User findUserByEmail(String email);

}
