package server.api.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
public class Question {

    public Question(String question, String author, String correct_answer, List<IncorrectAnswer> incorrect_answers) {
        this.question = question;
        this.author = author;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
    }

    protected Question() {}

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id;

    @NotNull
    String question;

    @NotNull
    String correct_answer;

    @OneToMany(orphanRemoval=true)
    @JoinColumn(name="question")
    List<IncorrectAnswer> incorrect_answers;

    @NotNull
    String author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }

    public void setCorrectAnswer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public List<IncorrectAnswer> getIncorrectAnswers() {
        return incorrect_answers;
    }

    public void setIncorrectAnswers(List<IncorrectAnswer> incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
