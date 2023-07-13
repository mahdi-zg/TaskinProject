package devteam.tn.taskin.Controllers;

import devteam.tn.taskin.Entities.Booking;
import devteam.tn.taskin.Exceptions.domain.NotAnImageFileException;
import devteam.tn.taskin.Services.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/add")
    public ResponseEntity<Booking> addBooking(@RequestParam("fullName") String fullName,
                                              @RequestParam("cinNbr") String cinNbr,
                                              @RequestParam("dateNaissance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
                                              @RequestParam("codePostal") String codePostal,
                                              @RequestParam("imageProfile") MultipartFile imageProfile,
                                              @RequestParam("passport") MultipartFile passport,
                                              @RequestParam("residenceCard") MultipartFile residenceCard,
                                              @RequestParam("contratCdi") MultipartFile contratCdi,
                                              @RequestParam("payslip") MultipartFile payslip,
                                              @RequestParam("bankingReceipt") MultipartFile bankingReceipt,
                                              @RequestParam("taxNotice") MultipartFile taxNotice,
                                              @RequestParam("preferenceDate") String preferenceDate,
                                              @RequestParam("region") String region,
                                              @RequestParam("message") String message) throws IOException, NotAnImageFileException {
        Booking newBooking = bookingService.addBooking(fullName, cinNbr, dateNaissance, codePostal, imageProfile, passport, residenceCard,
                contratCdi, payslip, bankingReceipt, taxNotice, preferenceDate, region, message);
        return ResponseEntity.ok(newBooking);
    }
}
