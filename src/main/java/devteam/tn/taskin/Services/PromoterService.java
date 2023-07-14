package devteam.tn.taskin.Services;

import devteam.tn.taskin.Entities.Promoter;
import devteam.tn.taskin.Interfaces.IPromoterService;
import devteam.tn.taskin.Repository.PromoterRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromoterService implements IPromoterService {
    private final PromoterRepository promoterRepository;

    public PromoterService(PromoterRepository promoterRepository) {
        this.promoterRepository = promoterRepository;
    }

    @Override
    public Promoter createPromoter(String denomination, String governorate, String projectAddress, Integer capacity, LocalDate keyDate, Integer contractDuration) {
        Promoter promoter = new Promoter();
        promoter.setDenomination(denomination);
        promoter.setGovernorate(governorate);
        promoter.setProjectAdress(projectAddress);
        promoter.setCapacity(capacity);
        promoter.setKeyDate(keyDate);
        promoter.setContractDuration(contractDuration);
        return promoterRepository.save(promoter);
    }

    @Override
    public Promoter getPromoterById(Long promoterId) {
        return promoterRepository.findById(promoterId)
                .orElse(null);
    }

    @Override
    public List<Promoter> getAllPromoters() {
        return promoterRepository.findAll();
    }

    @Override
    public Promoter updatePromoter(Long id, Promoter updatedPromoter) {
        Promoter existingPromoteur = promoterRepository.findById(id).orElse(null);
        if (existingPromoteur == null) {
            throw new NotFoundException("Promoter not found with id: " + id);
        }

        existingPromoteur.setDenomination(updatedPromoter.getDenomination());
        existingPromoteur.setGovernorate(updatedPromoter.getGovernorate());
        existingPromoteur.setProjectAdress(updatedPromoter.getProjectAdress());
        existingPromoteur.setCapacity(updatedPromoter.getCapacity());
        existingPromoteur.setKeyDate(updatedPromoter.getKeyDate());
        existingPromoteur.setContractDuration(updatedPromoter.getContractDuration());

        return promoterRepository.save(existingPromoteur);
    }

    @Override
    public void deletePromoteur(Long id) {
        promoterRepository.deleteById(id);
    }
}
