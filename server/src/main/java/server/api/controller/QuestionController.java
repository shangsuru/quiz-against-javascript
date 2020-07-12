package server.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.exception.QuestionFormatException;
import server.api.model.Question;
import server.api.repository.AnswerRepository;
import server.api.repository.QuestionRepository;

import java.util.Collection;


@RestController
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @ExceptionHandler(QuestionFormatException.class)
    public ResponseEntity<String> handleQuestionFormatException(QuestionFormatException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/questions")
    public Collection<Question> getQuestions() {
        return questionRepository.getRandomQuestions();
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody JsonNode json) {
        if (!(json.has("question") && json.has("correct_answer") && json.has("incorrect_answers") && json.has("author"))) {
            throw new QuestionFormatException("Request body has to contain question, correct_answer, incorrect_answers, author fields");
        }

        if (json.get("incorrect_answers").size() != 3) {
            throw new QuestionFormatException("3 incorrect answers have to be provided!");
        }

        return questionRepository.saveQuestionWithAnswers(json, answerRepository);
    }

    @DeleteMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteQuestion(@PathVariable Integer id) {
        questionRepository.deleteById(id);
    }

}
