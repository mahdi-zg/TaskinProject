package devteam.tn.taskin.Interfaces;


import devteam.tn.taskin.Entities.User;
import devteam.tn.taskin.Enumeration.Gender;
import devteam.tn.taskin.Exceptions.domain.*;
import devteam.tn.taskin.Services.UserService;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface IUserService {

    User register(String firstName, String lastName, String email, String cin, String numTel, Gender gender) throws UserNotFoundException, EmailExistException, CinExistException, MessagingException;


    User addNewUser(String firstName, String lastName, String email, String numTel, String cin, Gender gender, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CinExistException, IOException, NotAnImageFileException;

    User updateUser(String currentCin, String newFirstName, String newLastName, String newCin, String newEmail,String newNumTel,Gender gender, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CinExistException, IOException, NotAnImageFileException;

    void resetPassword(String email) throws EmailNotFoundException, MessagingException;

    User updateProfileImage(String cin, MultipartFile profileImage) throws CinExistException, UserNotFoundException, EmailExistException, IOException, NotAnImageFileException;

    List<User> getUsers();

    User findUserByCin(String cin);

    User findUserByEmail(String email);

    void deleteUser(String cin) throws IOException;

    User getUserById(Long userId);
}
