package com.edumate.edumate.dto;

import com.edumate.edumate.entities.books.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookWithoutCoverDTO {
  private Integer id;
  private String title;
  private String isbn;

  private String authors;

  public static BookWithoutCoverDTO fromEntity(Book book) {
    return new BookWithoutCoverDTO(
        book.getId(),
        book.getTitle(),
        book.getIsbn(),
        book.getAuthors()
    );
  }
}
