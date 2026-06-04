package com.pao.proiect.elearning.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfață generică Repository — definește operațiile CRUD de bază.
 * @param <T>  tipul entității
 * @param <ID> tipul cheii primare
 */
public interface Repository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void update(T entity);
    void delete(ID id);
}
