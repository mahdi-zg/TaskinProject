package devteam.tn.taskin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBlog;
    private String title;
    private String content;
    private String imageBlog;

    @JsonIgnore
    @ManyToOne
    User userBlog;
}
