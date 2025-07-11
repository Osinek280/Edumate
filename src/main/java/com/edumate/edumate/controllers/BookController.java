package com.edumate.edumate.controllers;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

  private final BookRepository bookRepository;
  private final UnitRepository unitRepository;
  private final VocabularyRepository vocabularyRepository;

  // books
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Book> createBook(
      @RequestParam String title,
      @RequestParam String isbn,
      @RequestParam String authors,
      @RequestParam(required = false) MultipartFile coverImage
  ) throws IOException {
    Book book = Book.builder()
        .title(title)
        .isbn(isbn)
        .authors(authors)
        .coverImage(coverImage != null ? coverImage.getBytes() : null)
        .build();

    Book savedBook = bookRepository.save(book);
    return ResponseEntity.ok(savedBook);
  }

  @GetMapping
  public ResponseEntity<List<Book>> getAllBooks() {
    return ResponseEntity.ok(bookRepository.findAll());
  }

  @PostMapping(path = "/{bookId}/unit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Unit> addUnit(
      @PathVariable Integer bookId,
      @RequestParam Integer unitNumber,
      @RequestParam(required = false) MultipartFile wordlistImage
  ) throws IOException {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("Book not found"));

    Unit unit = Unit.builder()
        .unitNumber(unitNumber)
        .book(book)
        .wordlistImage(wordlistImage != null ? wordlistImage.getBytes() : null)
        .build();

    Unit savedUnit = unitRepository.save(unit);

    return ResponseEntity.ok(savedUnit);
  }

  @PostMapping("units/{unitId}/vocabulary")
  public ResponseEntity<?> addVocabularyToUnit(
      @PathVariable Integer unitId,
      @RequestBody @Valid List<JsonWord> vocabularyRequests) {

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
        .collect(Collectors.toList());

    vocabularyRepository.saveAll(vocabularies);

    return ResponseEntity.ok().build();
  }
}
