package devteam.tn.taskin.Interfaces;


import devteam.tn.taskin.Entities.Booking;
import devteam.tn.taskin.Exceptions.domain.NotAnImageFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

public interface IBookingService {

    Booking addBooking(String fullName, String cinNbr, LocalDate dateNaissance, String codePostal, MultipartFile imageProfile,
                       MultipartFile passport, MultipartFile residenceCard, MultipartFile contratCdi, MultipartFile payslip,
                       MultipartFile bankingReceipt, MultipartFile taxNotice, String preferenceDate, String region, String message)
            throws IOException, NotAnImageFileException;
}
