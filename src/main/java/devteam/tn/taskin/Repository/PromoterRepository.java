package devteam.tn.taskin.Repository;

import devteam.tn.taskin.Entities.Promoter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoterRepository extends JpaRepository<Promoter,Long> {
}
