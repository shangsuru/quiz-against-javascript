package server.api.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuestionFormatException extends RuntimeException {

    public QuestionFormatException(String error) {
        super("Question cannot be created: " + error);
    }

}
