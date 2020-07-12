package server.api.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Entity
public class Question {

    public Question(String text, String author, List<Answer> answers) {
        this.text = text;
        this.author = author;
        this.answers = answers;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String text;

    @OneToMany(orphanRemoval=true)
    @JoinColumn(name="question")
    private List<Answer> answers;

    @NotNull
    private String author;

}
