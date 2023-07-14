package devteam.tn.taskin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Building implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zone;
    private String gouvernorat;
    private String adresse;
    private String nbrBuilding;
    private LocalDate deleveryDate;

    @JsonIgnore
    @ManyToMany(mappedBy="buildings",cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="building")
    private Set<Promoter> promoters = new HashSet<>();

}
