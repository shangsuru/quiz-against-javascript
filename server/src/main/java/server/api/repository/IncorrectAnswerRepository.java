package server.api.repository;

import org.springframework.data.repository.CrudRepository;
import server.api.model.IncorrectAnswer;


public interface IncorrectAnswerRepository extends CrudRepository<IncorrectAnswer, Integer> {}
