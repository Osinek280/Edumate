package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.user.LearningStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edumate.edumate.entities.user.UserVocabulary;
import com.edumate.edumate.entities.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, Long>{
  List<UserVocabulary> findByUserEmailAndStatus(String email, LearningStatus status);
}
