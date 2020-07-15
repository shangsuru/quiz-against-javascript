package server.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import server.api.dto.QuestionDTO;
import server.api.repository.QuestionRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiApplicationTests {

    enum Mode { NORMAL, MISSING_FIELD, NOT_ENOUGH_ANSWERS }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(port).isNotNull();
        assertThat(restTemplate).isNotNull();
        assertThat(questionRepository).isNotNull();
    }

   @Test
   @Order(2)
    void inserts10QuestionsFromApi() {
        long oldCount = questionRepository.count();
        JsonNode response = this.restTemplate.getForObject("http://localhost:" + port + "/api", JsonNode.class);
        long newCount = questionRepository.count();
        assertThat(newCount).isEqualTo(oldCount + 10);
    }

    @Test
    @Order(3)
    void gets10Questions() {
        List<QuestionDTO> response = this.restTemplate.getForObject("http://localhost:" + port + "/questions", List.class);
        assertThat(response).hasSize(10);
    }

    @Test
    @Order(4)
    void savesCustomQuestion() throws JSONException {
        HttpEntity<String> question = this.getQuestionToPost(Mode.NORMAL);

        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/questions", question, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);

        JSONObject result = new JSONObject(response.getBody());
        assertThat(result.get("question")).isEqualTo("Test is working?");
        assertThat(result.get("author")).isEqualTo("developer");
        assertThat(result.get("correct_answer")).isEqualTo("YES");
        JSONArray wrong = (JSONArray) result.get("incorrect_answers");
        assertThat(wrong.length()).isEqualTo(3);
        assertThat(wrong.get(0)).isEqualTo("Not again");
        assertThat(wrong.get(1)).isEqualTo("Umm");
        assertThat(wrong.get(2)).isEqualTo("... (cries in corner)");
    }

    @Test
    @Order(5)
    void errorMissingField() throws JSONException {
        HttpEntity<String> question = this.getQuestionToPost(Mode.MISSING_FIELD);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/questions", question, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Question cannot be created: Request body has to contain question, correct_answer, incorrect_answers, author fields");
    }

    @Test
    @Order(6)
    void errorNotEnoughAnswers() throws JSONException {
        HttpEntity<String> question = this.getQuestionToPost(Mode.NOT_ENOUGH_ANSWERS);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/questions", question, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Question cannot be created: 3 wrong answers have to be provided!");
    }

    @Test
    @Order(6)
    void deletesQuestion() throws JSONException, URISyntaxException {
        HttpEntity<String> question = this.getQuestionToPost(Mode.NORMAL);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/questions", question, String.class);
        JSONObject result = new JSONObject(response.getBody());
        int id = (int) result.get("id");
        assertThat(questionRepository.findById(id).isPresent()).isTrue();
        this.restTemplate.delete(new URI("http://localhost:" + port + "/questions/" + id));
        assertThat(questionRepository.findById(id).isPresent()).isFalse();
    }

    private HttpEntity<String> getQuestionToPost(Mode mode) throws JSONException {
        JSONObject question = new JSONObject();
        question.put("question", "Test is working?");
        question.put("correct_answer", "YES");
        JSONArray incorrectAnswers = new JSONArray();
        incorrectAnswers.put("Not again");
        incorrectAnswers.put("Umm");

        // omit third answer for this test
        if (!mode.equals(Mode.NOT_ENOUGH_ANSWERS)) {
            incorrectAnswers.put("... (cries in corner)");
        }

        question.put("incorrect_answers", incorrectAnswers);

        // omit author field for this test
        if (!mode.equals(Mode.MISSING_FIELD)) {
            question.put("author", "developer");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<String>(question.toString(), headers);
    }

}
