package devteam.tn.taskin.Services;

import devteam.tn.taskin.Entities.Claim;
import devteam.tn.taskin.Repository.ClaimRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;

    public ClaimService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public Claim createClaim(Claim claim) {
        return claimRepository.save(claim);
    }

    public Claim getClaimById(Long id) {
        return claimRepository.findById(id).orElse(null);
    }

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public Claim updateClaim(Long id, Claim updatedClaim) {
        Claim existingClaim = claimRepository.findById(id).orElse(null);
        if (existingClaim != null) {
            existingClaim.setSubject(updatedClaim.getSubject());
            existingClaim.setDescription(updatedClaim.getDescription());
            existingClaim.setDateClaim(updatedClaim.getDateClaim());
            existingClaim.setUserClaim(updatedClaim.getUserClaim());
            return claimRepository.save(existingClaim);
        }
        return null;
    }

    public void deleteClaim(Long id) {
        claimRepository.deleteById(id);
    }
}

