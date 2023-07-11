package devteam.tn.taskin.Services;

import devteam.tn.taskin.Interfaces.IPromoterService;
import devteam.tn.taskin.Repository.PromoterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PromoterService implements IPromoterService {

    @Autowired
    PromoterRepository promoterRepository;


}
