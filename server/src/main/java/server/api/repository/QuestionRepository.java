package server.api.repository;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import server.api.model.Answer;
import server.api.model.Question;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public interface QuestionRepository extends CrudRepository<Question, Integer> {

    @Query(value = "SELECT * from question ORDER BY RAND() LIMIT 10", nativeQuery = true)
    Collection<Question> getRandomQuestions();

    default Question saveQuestionWithAnswers(JsonNode json, AnswerRepository answerRepository) {
        String questionText = json.get("question").asText();
        String author = json.has("author") ? json.get("author").asText() : "API";
        ArrayList<Answer> answers = new ArrayList<>();

        Answer correctAnswer = new Answer(json.get("correct_answer").asText(), true);
        answers.add(correctAnswer);
        answerRepository.save(correctAnswer);

        for (JsonNode answer: json.get("incorrect_answers")) {
            Answer wrongAnswer = new Answer(answer.asText(), false);
            answers.add(wrongAnswer);
            answerRepository.save(wrongAnswer);
        }

        Question question = new Question(questionText, author, answers);
        return this.save(question);

    }

}
