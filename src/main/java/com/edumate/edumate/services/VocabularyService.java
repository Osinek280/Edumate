package com.edumate.edumate.services;


import com.edumate.edumate.entities.user.Level;
import com.edumate.edumate.entities.user.Vocabulary;
import com.edumate.edumate.repositories.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VocabularyService {
  private final VocabularyRepository vocabularyRepository;

  public Page<Vocabulary> getVocabulary(Level level, Pageable pageable) {
    if(level != null) {
      return vocabularyRepository.findAllByLevel(level, pageable);
    } else {
      return vocabularyRepository.findAll(pageable);
    }
  }

  public long countByLevel(Level level) {
    return vocabularyRepository.countByLevel(level);
  }
}
