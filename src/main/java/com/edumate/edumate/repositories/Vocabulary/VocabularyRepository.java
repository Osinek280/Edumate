package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.user.LearningStatus;
import com.edumate.edumate.entities.user.Level;
import com.edumate.edumate.entities.user.Vocabulary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
  Page<Vocabulary> findAllByLevel(Level level, Pageable pageable);
  Page<Vocabulary> findByIdNotInAndLevel(List<Long> ids, Level level, Pageable pageable);
  Page<Vocabulary> findByIdNotIn(List<Long> ids, Pageable pageable);
  long countByLevel(Level level);
}
