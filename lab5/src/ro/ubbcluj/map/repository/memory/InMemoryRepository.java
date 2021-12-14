package ro.ubbcluj.map.repository.memory;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validators.Validator;
import java.util.HashMap;
import java.util.Map;
import ro.ubbcluj.map.repository.Repository;


public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Validator<E> validator;
    protected Map<ID, E> entities;
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }


    public Map<ID, E> getEntities() {
        return this.entities;
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            return entity;
        } else entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if (entities.get(id) != null) {
            entities.remove(id);
            return entities.get(id);
        } else {
            throw new IllegalArgumentException("deleted entity doesn't exist");
        }
    }

    @Override
    public E update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        entities.put(entity.getId(), entity);
        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }

}