package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.user.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
}
