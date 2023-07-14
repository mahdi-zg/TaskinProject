package devteam.tn.taskin.Controllers;


import devteam.tn.taskin.Entities.Building;
import devteam.tn.taskin.Entities.Promoter;
import devteam.tn.taskin.Entities.User;
import devteam.tn.taskin.Interfaces.IBuildingService;
import devteam.tn.taskin.Interfaces.IPromoterService;
import devteam.tn.taskin.Interfaces.IUserService;
import devteam.tn.taskin.Services.BuildingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/buildings")
public class BuildingController {
    private final IBuildingService iBuildingService;

    private final IPromoterService iPromoterService;

   private final IUserService iUserService;

    public BuildingController(IBuildingService iBuildingService, IPromoterService iPromoterService, IUserService iUserService) {
        this.iBuildingService = iBuildingService;
        this.iPromoterService = iPromoterService;
        this.iUserService = iUserService;
    }

    @PostMapping("/add")
    public ResponseEntity<Building> createBuilding(@RequestBody Building buildingRequest,
                                                   @RequestParam("promoterId") Long promoterId) {
        Building createdBuilding = iBuildingService.createBuilding(
                buildingRequest.getZone(),
                buildingRequest.getGouvernorat(),
                buildingRequest.getAdresse(),
                buildingRequest.getNbrBuilding(),
                buildingRequest.getDeleveryDate()
        );

        iBuildingService.assignBuildingToPromoter(createdBuilding.getId(), promoterId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuilding);
    }

    @PostMapping("/{buildingId}/assign-user")
    public ResponseEntity<Building> assignUserToBuilding(@PathVariable("buildingId") Long buildingId, @RequestParam("userId") Long userId) {
        // Vérifier si le bâtiment existe
        Building building = iBuildingService.getBuildingById(buildingId);
        if (building == null) {
            return ResponseEntity.notFound().build();
        }

        // Vérifier si l'utilisateur existe
        User user = iUserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Appeler le service pour affecter l'utilisateur au bâtiment
        Building updatedBuilding = iBuildingService.assignBuildingToUser(buildingId, userId);

        return ResponseEntity.ok(updatedBuilding);
    }






    @GetMapping("getBuilding/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Long id) {
        Building building = iBuildingService.getBuildingById(id);
        if (building == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(building);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable Long id, @RequestBody Building building) {
        Building updatedBuilding = iBuildingService.updateBuilding(id, building);
        if (updatedBuilding == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBuilding);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        iBuildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
}
