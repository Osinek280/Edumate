package com.edumate.edumate.repositories.Vocabulary;

import com.edumate.edumate.entities.vocabulary.LearningStatus;
import com.edumate.edumate.entities.vocabulary.Level;
import com.edumate.edumate.entities.vocabulary.UserVocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, Long>{
  @Query("SELECT uv.vocabulary.id FROM UserVocabulary uv WHERE uv.user.email = :userEmail")
  List<Long> findVocabularyIdsByUserEmail(@Param("userEmail") String userEmail);
  List<UserVocabulary> findByUserEmailAndStatus(String email, LearningStatus status, Pageable pageable);
  long countByUserEmailAndStatus(String email, LearningStatus status);
  long countByUserEmail(String email);

  Page<UserVocabulary> findByUserEmailAndStatusAndVocabulary_Level(
      String userEmail,
      LearningStatus status,
      Level level,
      Pageable pageable
  );
}
