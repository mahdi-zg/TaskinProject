package devteam.tn.taskin.Controllers;

import devteam.tn.taskin.Entities.Promoter;
import devteam.tn.taskin.Services.PromoterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promoters")
public class PromoterController {
    private final PromoterService promoterService;

    public PromoterController(PromoterService promoterService) {
        this.promoterService = promoterService;
    }

    @PostMapping("/add")
    public ResponseEntity<Promoter> createPromoter(@RequestBody Promoter promoter) {
        Promoter createdPromoter = promoterService.createPromoter(
                promoter.getDenomination(),
                promoter.getGovernorate(),
                promoter.getProjectAdress(),
                promoter.getCapacity(),
                promoter.getKeyDate(),
                promoter.getContractDuration()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPromoter);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<Promoter> getPromoterById(@PathVariable("id") Long id) {
        Promoter promoter = promoterService.getPromoterById(id);
        if (promoter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promoter);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Promoter>> getAllPromoters() {
        List<Promoter> promoters = promoterService.getAllPromoters();
        return ResponseEntity.ok(promoters);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Promoter> updatePromoter(@PathVariable("id") Long id, @RequestBody Promoter promoter) {
        Promoter updatedPromoter = promoterService.updatePromoter(id, promoter);
        return ResponseEntity.ok(updatedPromoter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromoter(@PathVariable("id") Long id) {
        promoterService.deletePromoteur(id);
        return ResponseEntity.noContent().build();
    }
}
