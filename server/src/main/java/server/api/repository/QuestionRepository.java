package server.api.repository;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import server.api.model.IncorrectAnswer;
import server.api.model.Question;
import java.util.ArrayList;
import java.util.Collection;


public interface QuestionRepository extends CrudRepository<Question, Integer> {

    @Query(value = "SELECT * from question ORDER BY RAND() LIMIT 10", nativeQuery = true)
    Collection<Question> getRandomQuestions();

    default Question saveQuestionWithAnswers(JsonNode json, IncorrectAnswerRepository incorrectAnswerRepository) {
        String question = json.get("question").asText();
        String author = json.has("author") ? json.get("author").asText() : "API";
        String correctAnswer = json.get("correct_answer").asText();
        ArrayList<IncorrectAnswer> incorrectAnswers = new ArrayList<>();

        for (JsonNode answer: json.get("incorrect_answers")) {
            IncorrectAnswer w = new IncorrectAnswer(answer.asText());
            incorrectAnswers.add(w);
            incorrectAnswerRepository.save(w);
        }

        return this.save(new Question(question, author, correctAnswer, incorrectAnswers));
    }

}
