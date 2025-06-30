package com.edumate.edumate.controllers;

import com.edumate.edumate.entities.vocabulary.UserVocabulary;
import com.edumate.edumate.repositories.Vocabulary.UserVocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flashcards")
@RequiredArgsConstructor
public class FlashcardController {

  private final UserVocabularyRepository userVocabularyRepository;

  @PostMapping("/{id}/review")
  public ResponseEntity<UserVocabulary> logReview(
      @PathVariable Long id,
      @RequestParam int rating
  ) {
    UserVocabulary card = userVocabularyRepository.findById(id).orElseThrow();
//    scheduler.scheduleNextReview(card, rating);
//    flashcardRepo.save(card);


    return ResponseEntity.ok(card);
  }

}
