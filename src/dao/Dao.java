package dao;

import java.util.List;
import java.util.Optional;

public interface Dao <E,K>{

    E save (E entity);

    Optional<E> findById(K id);

    List<E> findAll();

    boolean delete(K id);

    void update(E entity);


}
