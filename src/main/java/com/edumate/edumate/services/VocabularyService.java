package com.edumate.edumate.services;


import com.edumate.edumate.dto.VocabularyStatusCountDto;
import com.edumate.edumate.entities.user.AppUser;
import com.edumate.edumate.entities.vocabulary.LearningStatus;
import com.edumate.edumate.entities.vocabulary.Level;
import com.edumate.edumate.entities.vocabulary.UserVocabulary;
import com.edumate.edumate.entities.vocabulary.Vocabulary;
import com.edumate.edumate.repositories.UserRepository;
import com.edumate.edumate.repositories.Vocabulary.UserVocabularyRepository;
import com.edumate.edumate.repositories.Vocabulary.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabularyService {
  private final VocabularyRepository vocabularyRepository;
  private final UserVocabularyRepository userVocabularyRepository;
  private final UserRepository userRepository;

  @Async
  public void initializeUserVocabulary(String userEmail, Level level) {
    AppUser user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
    List<Vocabulary> allWords = vocabularyRepository.findAll();

    List<UserVocabulary> userVocabularyList = allWords.stream()
        .filter(vocab -> vocab.getLevel() != null)
        .filter(vocab -> vocab.getLevel().compareTo(level) <= 0) 
        .map(vocab -> {
          LearningStatus status = vocab.getLevel().compareTo(level) < 0
              ? LearningStatus.KNOWN
              : LearningStatus.LEARNING;

          return UserVocabulary.builder()
              .user(user)
              .vocabulary(vocab)
              .status(status)
              .difficulty(0.3)
              .easiness(2.5)
              .repetitions(0)
              .nextReviewDate(LocalDate.now())
              .build();
        })
        .toList();

    userVocabularyRepository.saveAll(userVocabularyList);
  }

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
