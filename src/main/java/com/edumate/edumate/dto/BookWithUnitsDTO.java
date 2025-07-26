package com.edumate.edumate.dto;

import com.edumate.edumate.entities.books.Book;
import com.edumate.edumate.entities.books.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookWithUnitsDTO {
  private BookWithoutCoverDTO book;
  private List<Unit> units;
}
