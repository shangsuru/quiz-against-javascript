package server.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import server.api.repository.AnswerRepository;
import server.api.repository.QuestionRepository;

@RestController
public class ApiController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/api")
    public JsonNode fetchExternalQuestions() {
        WebClient client = WebClient.create("https://opentdb.com/api.php?amount=10&type=multiple");
        JsonNode results =  client.get().retrieve().bodyToMono(JsonNode.class).block().get("results");

        for (JsonNode json: results) {
            questionRepository.saveQuestionWithAnswers(json, answerRepository);
        }

        return results;
    }

}
