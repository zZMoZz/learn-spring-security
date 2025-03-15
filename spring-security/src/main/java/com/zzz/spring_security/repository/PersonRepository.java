package com.zzz.spring_security.repository;

import com.zzz.spring_security.entity.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// we extend "CrudRepository" not "JpaRepository", because we don't need additional methods that JpaRepository provides
public interface PersonRepository extends CrudRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);
}
