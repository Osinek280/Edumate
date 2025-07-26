package com.edumate.edumate.controllers;

import com.edumate.edumate.dto.BookWithUnitsDTO;
import com.edumate.edumate.entities.books.Book;
import com.edumate.edumate.entities.books.Unit;
import com.edumate.edumate.entities.vocabulary.Level;
import com.edumate.edumate.entities.vocabulary.Vocabulary;
import com.edumate.edumate.repositories.BookRepository;
import com.edumate.edumate.repositories.UnitRepository;
import com.edumate.edumate.repositories.Vocabulary.VocabularyRepository;
import com.edumate.edumate.services.BookService;
import com.edumate.edumate.utility.JsonWord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Book> createBook(
      @RequestParam String title,
      @RequestParam String isbn,
      @RequestParam String authors,
      @RequestParam(required = false) MultipartFile coverImage
  ) throws IOException {
    Book savedBook = bookService.createBook(title, isbn, authors, coverImage);
    return ResponseEntity.ok(savedBook);
  }

  @GetMapping
  public ResponseEntity<List<Book>> getAllBooks(
      @RequestParam(required = false) String search,
      @ParameterObject Pageable pageable
  ) {
    List<Book> books = bookService.getAllBooks(pageable, search);
    return ResponseEntity.ok(books);
  }

  @GetMapping(path = "/{bookId}/unit")
  public ResponseEntity<BookWithUnitsDTO> getUnitsWithBook(
      @PathVariable Integer bookId
  ) throws IOException {
    return ResponseEntity.ok(bookService.getUnitsWithBook(bookId));
  }

  @PostMapping(path = "/{bookId}/unit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Unit> addUnit(
      @PathVariable Integer bookId,
      @RequestParam Integer unitNumber,
      @RequestParam(required = false) MultipartFile wordlistImage
  ) throws IOException {
    Unit savedUnit = bookService.addUnitToBook(bookId, unitNumber, wordlistImage);
    return ResponseEntity.ok(savedUnit);
  }

  @PostMapping("units/{unitId}/vocabulary")
  public ResponseEntity<?> addVocabularyToUnit(
      @PathVariable Integer unitId,
      @RequestBody @Valid List<JsonWord> vocabularyRequests) {
    bookService.addVocabularyToUnit(unitId, vocabularyRequests);
    return ResponseEntity.ok().build();
  }
}
