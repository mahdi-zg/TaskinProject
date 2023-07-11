package devteam.tn.taskin.Services;

import devteam.tn.taskin.Interfaces.IBookingService;
import devteam.tn.taskin.Repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingService implements IBookingService {

    @Autowired
    BookingRepository bookingRepository;


}
