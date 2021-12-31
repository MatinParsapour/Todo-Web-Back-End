package web.todo.ToDoWeb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.*;

public interface BaseService<E, ID extends Serializable> {

    E save(E e);

    List<E> saveAll(Collection<E> collection);

    Optional<E> findById(ID id);

    List<E> findAll();

    Page<E> findAll(Pageable pageable);

    void deleteById(ID id);
}
