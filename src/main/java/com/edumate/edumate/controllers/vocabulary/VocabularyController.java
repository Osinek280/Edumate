package com.edumate.edumate.controllers.vocabulary;

import com.edumate.edumate.dto.VocabularyStatusCountDto;
import com.edumate.edumate.entities.vocabulary.LearningStatus;
import com.edumate.edumate.entities.vocabulary.Level;
import com.edumate.edumate.entities.vocabulary.Vocabulary;
import com.edumate.edumate.services.VocabularyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vocabulary")
@RequiredArgsConstructor
@Tag(name = "Vocabulary", description = "API for retrieving vocabulary words")
public class VocabularyController {
  private final VocabularyService vocabularyService;

//  @SecurityRequirement(name = "bearer-jwt")
//  @GetMapping
//  @Operation(summary = "Get vocabulary list with pagination and optional level filter")
//  public Page<Vocabulary> getVocabulary(
//      @Parameter(description = "Level filter: a1, a2, b1, b2, c1, c2")
//      @RequestParam(required = false) Level level,
//      @ParameterObject Pageable pageable) {
//    return vocabularyService.getVocabulary(level, pageable);
//  }

  @SecurityRequirement(name = "bearer-jwt")
  @GetMapping("/countByStatus")
  @Operation(summary = "Get count of vocabulary words for a given status")
  public VocabularyStatusCountDto countUserVocabularyByStatus(
      @AuthenticationPrincipal UserDetails user) {
    return vocabularyService.countByStatus(user.getUsername());
  }

  @SecurityRequirement(name = "bearer-jwt")
  @GetMapping("/count")
  @Operation(summary = "Get count of vocabulary words for a given level")
  public long countVocabularyByLevel(
      @Parameter(description = "Level filter: a1, a2, b1, b2, c1, c2", required = true)
      @RequestParam Level level) {
    return vocabularyService.countByLevel(level);
  }
  @SecurityRequirement(name = "bearer-jwt")
  @GetMapping
  @Operation(summary = "Get known vocabulary words by user, optional level/status filter")
  public ResponseEntity<List<Vocabulary>> getKnownWords(
      @AuthenticationPrincipal UserDetails user,
      @Parameter(description = "Level filter: a1, a2, b1, b2, c1, c2")
      @RequestParam(required = false) Level level,
      @Parameter(description = "Learning status filter: known, unknown, learning")
      @RequestParam(required = false) LearningStatus status,
      @ParameterObject Pageable pageable
  ) {
    List<Vocabulary> knownWords = vocabularyService.getVocabularyByStatus(user.getUsername(), status, level, pageable);
    return ResponseEntity.ok(knownWords);
  }

}
