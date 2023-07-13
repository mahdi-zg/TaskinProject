package devteam.tn.taskin.Services;

import devteam.tn.taskin.Constant.FileConstant;
import devteam.tn.taskin.Entities.Booking;
import devteam.tn.taskin.Entities.User;
import devteam.tn.taskin.Enumeration.Status;
import devteam.tn.taskin.Exceptions.domain.NotAnImageFileException;
import devteam.tn.taskin.Interfaces.IBookingService;
import devteam.tn.taskin.Repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static devteam.tn.taskin.Constant.FileConstant.*;
import static devteam.tn.taskin.Constant.FileConstant.JPG_EXTENSION;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;

@Service
public class BookingService implements IBookingService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }



    private static final String FILE_DIRECTORY = "files";

    public Booking addBooking(String fullName, String cinNbr, LocalDate dateNaissance, String codePostal, MultipartFile imageProfile,
                              MultipartFile passport, MultipartFile residenceCard, MultipartFile contratCdi, MultipartFile payslip,
                              MultipartFile bankingReceipt, MultipartFile taxNotice, String preferenceDate, String region, String message) throws IOException {

        Booking booking = new Booking();
        // Définir les autres attributs du booking
        booking.setFullName(fullName);
        booking.setCinNbr(cinNbr);
        booking.setDateNaissance(dateNaissance);
        booking.setCodePostal(codePostal);
        booking.setPreferenceDate(preferenceDate);
        booking.setRegion(region);
        booking.setMessage(message);
        booking.setStatus(Status.untreated); // Définir l'état par défaut comme "Untreated"

        String bookingFolderName = fullName.toLowerCase().replaceAll("\\s+", ""); // Utilise le nom complet en minuscules sans espaces comme nom de dossier
        String bookingFolderPath = FILE_DIRECTORY + "/" + bookingFolderName;
        Path bookingDirectoryPath = createDirectory(bookingFolderPath);

        saveImageForAttribute(bookingDirectoryPath, "imageProfile", imageProfile, booking);
        saveImageForAttribute(bookingDirectoryPath, "passport", passport, booking);
        saveImageForAttribute(bookingDirectoryPath, "residenceCard", residenceCard, booking);
        saveImageForAttribute(bookingDirectoryPath, "contratCdi", contratCdi, booking);
        saveImageForAttribute(bookingDirectoryPath, "payslip", payslip, booking);
        saveImageForAttribute(bookingDirectoryPath, "bankingReceipt", bankingReceipt, booking);
        saveImageForAttribute(bookingDirectoryPath, "taxNotice", taxNotice, booking);

        // Assigner les autres valeurs d'attributs au booking

        // Enregistrer le booking dans la base de données
        bookingRepository.save(booking);

        return booking;
    }

    private void saveImageForAttribute(Path bookingDirectoryPath, String attributeName, MultipartFile image, Booking booking) throws IOException {
        // Obtenir le nom de fichier et le chemin complet
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        String renamedFileName = attributeName + getFileExtension(fileName);
        Path attributeFolderPath = bookingDirectoryPath.resolve(attributeName);
        Path filePath = attributeFolderPath.resolve(renamedFileName);

        // Créer le dossier de l'attribut s'il n'existe pas
        if (!Files.exists(attributeFolderPath)) {
            Files.createDirectories(attributeFolderPath);
        }

        // Enregistrer l'image dans le dossier de l'attribut
        try (InputStream inputStream = image.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Image enregistrée pour l'attribut " + attributeName + ": " + filePath);

        // Chemin absolu du dossier principal du booking
        Path absoluteBookingDirectoryPath = bookingDirectoryPath.toAbsolutePath();
        System.out.println("Chemin absolu du dossier principal du booking: " + absoluteBookingDirectoryPath);

        // Enregistrer le chemin de l'image dans l'objet Booking
        String imagePath = filePath.toString();
        setAttributeImageURL(booking, attributeName, imagePath);
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        }
        return "";
    }


    private Path createDirectory(String directoryPath) throws IOException {
        // Créer le dossier s'il n'existe pas
        Path path = Paths.get(directoryPath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("Dossier créé : " + path);
        }
        return path;
    }

    private void setAttributeImageURL(Booking booking, String attributeName, String imageURL) {
        // Mettre à jour l'URL de l'image dans l'entité Booking
        // Par exemple, en utilisant des setters appropriés
        if (attributeName.equalsIgnoreCase("imageProfile")) {
            booking.setImageProfile(imageURL);
        } else if (attributeName.equalsIgnoreCase("passport")) {
            booking.setPassport(imageURL);
        } else if (attributeName.equalsIgnoreCase("residenceCard")) {
            booking.setResidenceCard(imageURL);
        } else if (attributeName.equalsIgnoreCase("contratCdi")) {
            booking.setContratCdi(imageURL);
        } else if (attributeName.equalsIgnoreCase("payslip")) {
            booking.setPayslip(imageURL);
        } else if (attributeName.equalsIgnoreCase("bankingReceipt")) {
            booking.setBankingReceipt(imageURL);
        } else if (attributeName.equalsIgnoreCase("taxNotice")) {
            booking.setTaxNotice(imageURL);
        }
        // Ajouter des conditions pour les autres attributs d'image
    }
}
