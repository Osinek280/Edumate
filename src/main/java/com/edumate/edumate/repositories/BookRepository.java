package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
  Optional<Book> findByIsbn(String isbn);

  Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
