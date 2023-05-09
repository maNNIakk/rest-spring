package br.com.restspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.restspring.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {}
