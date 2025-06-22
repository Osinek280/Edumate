package com.edumate.edumate.services;


import com.edumate.edumate.dto.auth.VocabularyStatusCountDto;
import com.edumate.edumate.entities.vocabulary.LearningStatus;
import com.edumate.edumate.entities.vocabulary.Level;
import com.edumate.edumate.entities.vocabulary.UserVocabulary;
import com.edumate.edumate.entities.vocabulary.Vocabulary;
import com.edumate.edumate.repositories.Vocabulary.UserVocabularyRepository;
import com.edumate.edumate.repositories.Vocabulary.VocabularyRepository;
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

//  public Page<Vocabulary> getVocabulary(Level level, Pageable pageable) {
//    if(level != null) {
//      return vocabularyRepository.findAllByLevel(level, pageable);
//    } else {
//      return vocabularyRepository.findAll(pageable);
//    }
//  }

  public List<Vocabulary> getVocabularyByStatus(String userEmail, LearningStatus status, Level level, Pageable pageable) {
    if(status == null) {
      if(level != null) {
        return vocabularyRepository.findAllByLevel(level, pageable).getContent();
      }else {
        return vocabularyRepository.findAll(pageable).getContent();
      }
    }
    else if (status == LearningStatus.UNKNOWN) {
      List<Long> userVocabularyIds = userVocabularyRepository.findVocabularyIdsByUserEmail(userEmail);
      if (userVocabularyIds.isEmpty()) {
        if(level != null) {
          return vocabularyRepository.findAllByLevel(level, pageable).getContent();
        }else {
          return vocabularyRepository.findAll(pageable).getContent();
        }
      }
      else if (level != null) {
        return vocabularyRepository.findByIdNotInAndLevel(userVocabularyIds, level, pageable).getContent();
      } else {
        return vocabularyRepository.findByIdNotIn(userVocabularyIds, pageable).getContent();
      }
    } else {
      if (level != null) {
        return userVocabularyRepository
            .findByUserEmailAndStatusAndVocabulary_Level(userEmail, status, level, pageable)
            .stream()
            .map(UserVocabulary::getVocabulary)
            .toList();
      } else {
        return userVocabularyRepository
            .findByUserEmailAndStatus(userEmail, status, pageable)
            .stream()
            .map(UserVocabulary::getVocabulary)
            .toList();
      }
    }
  }

  public VocabularyStatusCountDto countByStatus(String username) {
    long known = userVocabularyRepository.countByUserEmailAndStatus(username, LearningStatus.KNOWN);
    long unknown = vocabularyRepository.count() - userVocabularyRepository.countByUserEmail(username);
    long learning = userVocabularyRepository.countByUserEmailAndStatus(username, LearningStatus.UNKNOWN);
    return new VocabularyStatusCountDto(known, unknown, learning);
  }

  public long countByLevel(Level level) {
    return vocabularyRepository.countByLevel(level);
  }
}
