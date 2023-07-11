package devteam.tn.taskin.Services;
import devteam.tn.taskin.Constant.FileConstant;
import devteam.tn.taskin.Entities.Role;
import devteam.tn.taskin.Entities.User;
import devteam.tn.taskin.Entities.UserPrincipal;
import devteam.tn.taskin.Enumeration.Gender;
import devteam.tn.taskin.Exceptions.domain.*;
import devteam.tn.taskin.Interfaces.IUserService;
import devteam.tn.taskin.Repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static devteam.tn.taskin.Constant.FileConstant.*;
import static devteam.tn.taskin.Constant.UserImplConstant.*;
import static devteam.tn.taskin.Entities.Role.ROLE_USER;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserService implements IUserService, UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    private EmailService emailService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String cin) throws UsernameNotFoundException {
        User user = userRepository.findUserByCin(cin);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_CIN + cin);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_CIN + cin);
        } else {

            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_CIN + cin);
            return userPrincipal;
        }
    }

    @Override
    public User register(String firstName, String lastName, String email, String cin, String numTel, Gender gender) throws UserNotFoundException, EmailExistException, CinExistException, MessagingException {
        validateNewCinAndEmail(EMPTY, cin, email);
        User user = new User();
        String password = generatePassword();
        user.setUserId(generateUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJoinDate(new Date());
        user.setCin(cin);
        user.setNumTel(numTel);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setGender(gender);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(null);
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        emailService.sendNewPasswordEmail(firstName, password, email);

        return user;
    }

    @Override
    public User addNewUser(String firstName, String lastName, String email, String numTel, String cin, Gender gender, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CinExistException, IOException, NotAnImageFileException {
        validateNewCinAndEmail(EMPTY, cin, email);
        User user = new User();
        String password = generatePassword();
        user.setUserId(generateUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJoinDate(new Date());
        user.setNumTel(numTel);
        user.setCin(cin);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setActive(isActive);
        user.setGender(gender);
        user.setNotLocked(isNonLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(cin));
        userRepository.save(user);
        saveProfileImage(user, profileImage);
        LOGGER.info("New user password: " + password);
        return user;
    }

    @Override
    public User updateUser(String currentCin, String newFirstName, String newLastName, String newCin, String newEmail,String newNumTel,Gender gender, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, CinExistException, IOException, NotAnImageFileException {
        User currentUser = validateNewCinAndEmail(currentCin, newCin, newEmail);
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setCin(newCin);
        currentUser.setEmail(newEmail);
        currentUser.setNumTel(newNumTel);
        currentUser.setGender(gender);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);
        return currentUser;
    }


    @Override
    public void resetPassword(String email) throws EmailNotFoundException, MessagingException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        emailService.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws CinExistException, UserNotFoundException, EmailExistException, IOException, NotAnImageFileException {
        User user = validateNewCinAndEmail(username, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByCin(String cin) {
        return userRepository.findUserByCin(cin);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void deleteUser(String cin) throws IOException {
        User user = userRepository.findUserByCin(cin);
        userRepository.deleteById(user.getId());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getCin()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getCin() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getCin() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getCin()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
                + username + DOT + JPG_EXTENSION).toUriString();
    }

    private User validateNewCinAndEmail(String currentCin, String newCin, String newEmail) throws UserNotFoundException, EmailExistException, CinExistException {
        User userByNewCin = findUserByCin(newCin);
        User userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentCin)) {
            User currentUser = findUserByCin(currentCin);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_CIN + currentCin);
            }
            if(userByNewCin != null && !currentUser.getId().equals(userByNewCin.getId())) {
                throw new CinExistException(CIN_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewCin != null) {
                throw new CinExistException(CIN_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }
}
