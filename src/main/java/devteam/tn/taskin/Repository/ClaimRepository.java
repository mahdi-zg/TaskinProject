package devteam.tn.taskin.Repository;
import devteam.tn.taskin.Entities.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRepository extends JpaRepository<Claim,Long> {
}
