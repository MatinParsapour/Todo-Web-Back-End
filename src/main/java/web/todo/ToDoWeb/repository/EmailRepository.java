package web.todo.ToDoWeb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Email;

import java.util.List;

public interface EmailRepository extends MongoRepository<Email, String> {

    List<Email> findAllByOriginAndIsDeletedFalse(String origin);

    List<Email> findAllByDestinationAndIsDeletedFalse(String origin);

    Email findByIdAndAndIsDeletedFalse(String emailId);
}
