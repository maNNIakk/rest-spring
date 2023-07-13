package br.com.restspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.restspring.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {}
