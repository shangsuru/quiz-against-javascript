package server.api.dto;

import java.util.List;
import java.util.stream.Collectors;


public class QuestionDTO {

    public QuestionDTO(Integer id, String question, String correct_answer, List<String> incorrect_answers, String author) {
        this.id = id;
        this.question = sanitizeInput(question);
        this.correct_answer = sanitizeInput(correct_answer);
        this.incorrect_answers = incorrect_answers.stream().map(this::sanitizeInput).collect(Collectors.toList());
        this.author = author;
    }

    private String sanitizeInput(String s) {
        return s.replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&eacute;", "e")
                .replace("&ouml;", "ö")
                .replace("&auml;", "ä")
                .replace("&uuml;", "ü")
                .replace("&ldquo;", "\"")
                .replace("&rdquo;", "\"");
    }

    private Integer id;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;
    private String author;

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

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }

    public void setIncorrect_answers(List<String> incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
