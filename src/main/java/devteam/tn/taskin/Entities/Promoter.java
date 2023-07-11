package devteam.tn.taskin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Promoter implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromoter;
    private String promoterId;
    private String denomination;
    private String governorate;
    private String projectAdress;
    private Integer capacity;
    private LocalDate keyDate;
    private Integer contractDuration;






    @JsonIgnore
    @ManyToOne
    Building building;

}
