package server.api.repository;

import org.springframework.data.repository.CrudRepository;
import server.api.model.Answer;


public interface AnswerRepository extends CrudRepository<Answer, Integer> {

}
