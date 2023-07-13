package devteam.tn.taskin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import devteam.tn.taskin.Enumeration.Status;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBooking;
    private String fullName;
    private String cinNbr;
    private LocalDate dateNaissance;
    private String codePostal;
    private String imageProfile;
    private String passport;
    private String residenceCard;
    private String contratCdi;
    private String payslip;
    private String bankingReceipt;
    private String taxNotice;
    private String preferenceDate;
    private String region;
    private String message;
    private Status status;

    @JsonIgnore
    @OneToOne(mappedBy="booking")
    private User user;

}
