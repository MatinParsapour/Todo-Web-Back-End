package web.todo.ToDoWeb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.model.Email;

import org.springframework.data.domain.Pageable;

public interface EmailRepository extends MongoRepository<Email, String> {

    Page<Email> findAllByOriginAndIsDeletedFalse(String origin, Pageable pageable);

    Page<Email> findAllByDestinationAndIsDeletedFalse(String origin, Pageable pageable);

    Email findByIdAndIsDeletedFalse(String emailId);
}
