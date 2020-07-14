package server.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.dto.QuestionDTO;
import server.api.exception.QuestionFormatException;
import server.api.model.IncorrectAnswer;
import server.api.model.Question;
import server.api.repository.IncorrectAnswerRepository;
import server.api.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class QuestionController {

    private QuestionRepository questionRepository;
    private IncorrectAnswerRepository incorrectAnswerRepository;

    public QuestionController(QuestionRepository questionRepository, IncorrectAnswerRepository incorrectAnswerRepository) {
        this.questionRepository = questionRepository;
        this.incorrectAnswerRepository = incorrectAnswerRepository;
    }

    @ExceptionHandler(QuestionFormatException.class)
    public ResponseEntity<String> handleQuestionFormatException(QuestionFormatException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/questions")
    public List<QuestionDTO> getQuestions() {
        Collection<Question> fetchedQuestions = questionRepository.getRandomQuestions();
        List<QuestionDTO>  questionList = new ArrayList<>();

        for (Question q: fetchedQuestions) {
            questionList.add(convertQuestionToDTO(q));
        }

        return questionList;
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO createQuestion(@RequestBody JsonNode json) {

        if (!(json.has("question") && json.has("correct_answer") && json.has("incorrect_answers") && json.has("author"))) {
            throw new QuestionFormatException("Request body has to contain question, correct_answer, incorrect_answers, author fields");
        }

        if (json.get("incorrect_answers").size() != 3) {
            throw new QuestionFormatException("3 wrong answers have to be provided!");
        }

        Question q = questionRepository.saveQuestionWithAnswers(json, incorrectAnswerRepository);
        return convertQuestionToDTO(q);
    }

    @DeleteMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteQuestion(@PathVariable Integer id) {
        questionRepository.deleteById(id);
    }

    private QuestionDTO convertQuestionToDTO(Question q) {
        List<String> incorrectAnswers = q.getIncorrectAnswers()
                .stream()
                .map(IncorrectAnswer::getText)
                .collect(Collectors.toList());

        return new QuestionDTO(q.getId(), q.getQuestion(), q.getCorrectAnswer(), incorrectAnswers, q.getAuthor());
    }

}
