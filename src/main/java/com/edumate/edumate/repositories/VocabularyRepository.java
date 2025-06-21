package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.user.Level;
import com.edumate.edumate.entities.user.Vocabulary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
  Page<Vocabulary> findAllByLevel(Level level, Pageable pageable);
  long countByLevel(Level level);
}
