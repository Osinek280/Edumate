package com.edumate.edumate.services;


import com.edumate.edumate.entities.user.LearningStatus;
import com.edumate.edumate.entities.user.Level;
import com.edumate.edumate.entities.user.UserVocabulary;
import com.edumate.edumate.entities.user.Vocabulary;
import com.edumate.edumate.repositories.UserVocabularyRepository;
import com.edumate.edumate.repositories.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabularyService {
  private final VocabularyRepository vocabularyRepository;
  private final UserVocabularyRepository userVocabularyRepository;

  public Page<Vocabulary> getVocabulary(Level level, Pageable pageable) {
    if(level != null) {
      return vocabularyRepository.findAllByLevel(level, pageable);
    } else {
      return vocabularyRepository.findAll(pageable);
    }
  }

  public List<Vocabulary> getKnownWords(String userEmail) {
    return userVocabularyRepository.findByUserEmailAndStatus(userEmail, LearningStatus.KNOWN)
        .stream()
        .map(UserVocabulary::getVocabulary)
        .toList();
  }

  public long countByLevel(Level level) {
    return vocabularyRepository.countByLevel(level);
  }
}
