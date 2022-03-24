package web.todo.ToDoWeb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Email;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EmailRepository extends MongoRepository<Email, String> {

    Page<Email> findAllByOriginAndIsDeletedFalse(String origin, Pageable pageable);

    Page<Email> findAllByDestinationAndIsDeletedFalse(String origin, Pageable pageable);

    Email findByIdAndAndIsDeletedFalse(String emailId);
}
