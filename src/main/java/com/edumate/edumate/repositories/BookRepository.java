package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
  Optional<Book> findByIsbn(String isbn);
}
