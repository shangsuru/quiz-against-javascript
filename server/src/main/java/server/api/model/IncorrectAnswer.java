package server.api.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
public class IncorrectAnswer {

    public IncorrectAnswer(String text) {
        this.text = text;
    }

    protected IncorrectAnswer() {}

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
