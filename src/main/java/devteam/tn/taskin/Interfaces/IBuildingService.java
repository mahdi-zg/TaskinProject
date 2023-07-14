package devteam.tn.taskin.Interfaces;

import devteam.tn.taskin.Entities.Building;

import java.time.LocalDate;

public interface IBuildingService {

    Building createBuilding(String zone, String gouvernorat, String adresse, String nbrBuilding, LocalDate deleveryDate);

    Building getBuildingById(Long id);

    Building updateBuilding(Long id, Building updatedBuilding);

    Building assignBuildingToPromoter(Long buildingId, Long promoterId);

    Building assignBuildingToUser(Long buildingId, Long userId);

    void deleteBuilding(Long buildingId);
}
