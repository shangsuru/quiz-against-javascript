package server.api.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Entity
public class Answer {

    public Answer(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String text;

    @NotBlank
    private boolean correct;

}
