package devteam.tn.taskin.Services;

import devteam.tn.taskin.Entities.Building;
import devteam.tn.taskin.Entities.Promoter;
import devteam.tn.taskin.Entities.User;
import devteam.tn.taskin.Interfaces.IBuildingService;
import devteam.tn.taskin.Repository.BuildingRepository;
import devteam.tn.taskin.Repository.PromoterRepository;
import devteam.tn.taskin.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;

@Service
public class BuildingService implements IBuildingService {


    private final BuildingRepository buildingRepository;
    private final PromoterRepository promoterRepository;
    private final UserRepository userRepository;

    public BuildingService(BuildingRepository buildingRepository, PromoterRepository promoterRepository, UserRepository userRepository) {
        this.buildingRepository = buildingRepository;
        this.promoterRepository = promoterRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Building createBuilding(String zone, String gouvernorat, String adresse, String nbrBuilding, LocalDate deleveryDate) {
        Building building = new Building();
        building.setZone(zone);
        building.setGouvernorat(gouvernorat);
        building.setAdresse(adresse);
        building.setNbrBuilding(nbrBuilding);
        building.setDeleveryDate(deleveryDate);
        return buildingRepository.save(building);
    }

    @Override
    public Building getBuildingById(Long id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Building not found with id: " + id));
}

    @Override
    public Building updateBuilding(Long id, Building updatedBuilding) {
        Building existingBuilding = buildingRepository.findById(id).orElse(null);
        if (existingBuilding == null) {
            throw new NotFoundException("Building not found with id: " + id);
        }

        // Mettre à jour les informations du bâtiment
        existingBuilding.setZone(updatedBuilding.getZone());
        existingBuilding.setGouvernorat(updatedBuilding.getGouvernorat());
        existingBuilding.setAdresse(updatedBuilding.getAdresse());
        existingBuilding.setNbrBuilding(updatedBuilding.getNbrBuilding());
        existingBuilding.setDeleveryDate(updatedBuilding.getDeleveryDate());

        // Enregistrer les modifications dans la base de données
        return buildingRepository.save(existingBuilding);
    }




    public Building assignBuildingToPromoter(Long buildingId, Long promoterId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new NotFoundException("Building not found with id: " + buildingId));
        Promoter promoter = promoterRepository.findById(promoterId)
                .orElseThrow(() -> new NotFoundException("Promoter not found with id: " + promoterId));

        building.getPromoters().add(promoter);
        promoter.setBuilding(building);

        buildingRepository.save(building);
        promoterRepository.save(promoter);

        return building;
    }

    @Override
    public Building assignBuildingToUser(Long buildingId, Long userId) {
        Building building = buildingRepository.findById(buildingId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (building != null && user != null) {
            building.getUsers().add(user);
            user.getBuildings().add(building);
            buildingRepository.save(building);
            userRepository.save(user);
        }
        return building;
    }


    @Override
    public  void deleteBuilding (Long buildingId){
        buildingRepository.deleteById(buildingId);
    }

}
