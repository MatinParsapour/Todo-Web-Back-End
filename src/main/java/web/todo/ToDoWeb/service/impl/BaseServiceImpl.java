package web.todo.ToDoWeb.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import web.todo.ToDoWeb.service.BaseService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BaseServiceImpl<E, ID extends Serializable, R extends MongoRepository<E,ID>> implements BaseService<E,ID> {

    protected final R repository;

    public BaseServiceImpl(R repository){
        this.repository = repository;
    }

    @Override
    public E save(E e) {
        return repository.save(e);
    }

    @Override
    public List<E> saveAll(Collection<E> collection) {
        return repository.saveAll(collection);
    }

    @Override
    public Optional<E> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
