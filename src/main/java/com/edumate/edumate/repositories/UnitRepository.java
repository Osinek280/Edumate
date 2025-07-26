package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.books.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
  List<Unit> findByBookId(Integer bookId);
}

