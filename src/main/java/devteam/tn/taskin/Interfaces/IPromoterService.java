package devteam.tn.taskin.Interfaces;


import devteam.tn.taskin.Entities.Promoter;

import java.time.LocalDate;
import java.util.List;

public interface IPromoterService {

    Promoter createPromoter(String denomination, String governorate, String projectAddress, Integer capacity, LocalDate keyDate, Integer contractDuration);

    Promoter getPromoterById(Long promoterId);

    List<Promoter> getAllPromoters();

    Promoter updatePromoter(Long id, Promoter updatedPromoter);

    void deletePromoteur(Long id);
}
