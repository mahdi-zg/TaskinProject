package devteam.tn.taskin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import devteam.tn.taskin.Enumeration.Gender;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String cin;
    private String NumTel;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Building> buildings;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="userBlog")
    private Set<Blog> blogs;

    @JsonIgnore
    @OneToOne
    private Booking booking;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="userClaim")
    private Set<Claim> claims;
}
