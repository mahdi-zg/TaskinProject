package devteam.tn.taskin.Services;

import devteam.tn.taskin.Interfaces.IBuildingService;
import devteam.tn.taskin.Repository.BuildingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImmobilierService implements IBuildingService {

    @Autowired
    BuildingRepository buildingRepository;

}
