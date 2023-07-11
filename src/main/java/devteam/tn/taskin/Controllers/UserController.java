package devteam.tn.taskin.Controllers;

import devteam.tn.taskin.Entities.User;
import devteam.tn.taskin.Entities.UserPrincipal;
import devteam.tn.taskin.Enumeration.Gender;
import devteam.tn.taskin.Exceptions.domain.CinExistException;
import devteam.tn.taskin.Exceptions.domain.EmailExistException;
import devteam.tn.taskin.Exceptions.domain.NotAnImageFileException;
import devteam.tn.taskin.Exceptions.domain.UserNotFoundException;
import devteam.tn.taskin.Interfaces.IUserService;
import devteam.tn.taskin.Utility.JWTTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

import java.io.IOException;

import static devteam.tn.taskin.Constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/", "/user"})
public class UserController {
    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private AuthenticationManager authenticationManager;
    private IUserService iUserService;
    private JWTTokenProvider jwtTokenProvider;

    public UserController(AuthenticationManager authenticationManager, IUserService iUserService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.iUserService = iUserService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getCin(), user.getPassword());
        User loginUser = iUserService.findUserByCin(user.getCin());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, CinExistException, MessagingException {
        User newUser = iUserService.register(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCin(),
                user.getNumTel(),
                user.getGender()
        );
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("cin") String cin,
                                           @RequestParam("email") String email,
                                           @RequestParam("numTel") String numTel,
                                           @RequestParam("gender") Gender gender,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, CinExistException, EmailExistException, IOException, NotAnImageFileException, CinExistException, NotAnImageFileException {
        User newUser = iUserService.addNewUser(
                firstName,
                lastName,
                cin,
                email,
                numTel,
                gender,
                role,
                Boolean.parseBoolean(isActive),
                Boolean.parseBoolean(isNonLocked),
                profileImage
        );
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestParam("currentCin") String currentCin,
                                       @RequestParam("firstName") String firstName,
                                       @RequestParam("lastName") String lastName,
                                       @RequestParam("cin") String cin,
                                       @RequestParam("email") String email,
                                       @RequestParam("numTel") String numTel,
                                       @RequestParam("gender") Gender gender,
                                       @RequestParam("role") String role,
                                       @RequestParam("isActive") String isActive,
                                       @RequestParam("isNonLocked") String isNonLocked,
                                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, CinExistException, EmailExistException, IOException, NotAnImageFileException, CinExistException {
        User updatedUser = iUserService.updateUser(
                currentCin,
                firstName,
                lastName,
                cin,
                email,
                numTel,
                gender,
                role,
                Boolean.parseBoolean(isActive),
                Boolean.parseBoolean(isNonLocked),
                profileImage
        );
        return new ResponseEntity<>(updatedUser, OK);
    }



    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }
    private void authenticate(String cin, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(cin, password));
    }

}
