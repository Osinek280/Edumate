package com.edumate.edumate.services;

import com.edumate.edumate.entities.books.Book;
import com.edumate.edumate.entities.books.Unit;
import com.edumate.edumate.entities.vocabulary.Level;
import com.edumate.edumate.entities.vocabulary.Vocabulary;
import com.edumate.edumate.repositories.BookRepository;
import com.edumate.edumate.repositories.UnitRepository;
import com.edumate.edumate.repositories.Vocabulary.VocabularyRepository;
import com.edumate.edumate.utility.JsonWord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final UnitRepository unitRepository;
  private final VocabularyRepository vocabularyRepository;

  public Book createBook(String title, String isbn, String authors, MultipartFile coverImage) throws IOException {
    Book book = Book.builder()
        .title(title)
        .isbn(isbn)
        .authors(authors)
        .coverImage(coverImage != null ? coverImage.getBytes() : null)
        .build();

    return bookRepository.save(book);
  }

  @Transactional
  public List<Book> getAllBooks(Pageable pageable, String searchQuery) {
    if (searchQuery != null && !searchQuery.isEmpty()) {
      return bookRepository.findByTitleContainingIgnoreCase(searchQuery, pageable).getContent();
    }
    return bookRepository.findAll(pageable).getContent();
  }

  public Unit addUnitToBook(Integer bookId, Integer unitNumber, MultipartFile wordlistImage) throws IOException {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("Book not found"));

    Unit unit = Unit.builder()
        .unitNumber(unitNumber)
        .book(book)
        .wordlistImage(wordlistImage != null ? wordlistImage.getBytes() : null)
        .build();

    return unitRepository.save(unit);
  }

  public void addVocabularyToUnit(Integer unitId, @Valid List<JsonWord> vocabularyRequests) {
    Unit unit = unitRepository.findById(unitId)
        .orElseThrow(() -> new RuntimeException("Unit not found with id: " + unitId));

    List<Vocabulary> vocabularies = vocabularyRequests.stream()
        .map(request -> {
          Vocabulary vocabulary = new Vocabulary();
          vocabulary.setWord(request.getEnglish());
          vocabulary.setPhonetic(request.getPhonetic());
          vocabulary.setTranslation(request.getTranslation());
          vocabulary.setLevel(Level.B1);
          vocabulary.addUnit(unit);
          return vocabulary;
        })
        .toList();

    vocabularyRepository.saveAll(vocabularies);
  }
}