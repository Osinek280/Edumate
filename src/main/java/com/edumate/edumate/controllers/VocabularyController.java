package com.edumate.edumate.controllers;

import com.edumate.edumate.entities.user.Level;
import com.edumate.edumate.entities.user.Vocabulary;
import com.edumate.edumate.services.VocabularyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vocabulary")
@RequiredArgsConstructor
@Tag(name = "Vocabulary", description = "API for retrieving vocabulary words")
public class VocabularyController {
  private final VocabularyService vocabularyService;

  @SecurityRequirement(name = "bearer-jwt")
  @GetMapping
  @Operation(summary = "Get vocabulary list with pagination and optional level filter")
  public Page<Vocabulary> getVocabulary(
      @Parameter(description = "Level filter: a1, a2, b1, b2, c1, c2")
      @RequestParam(required = false) Level level,
      @ParameterObject Pageable pageable) {
    return vocabularyService.getVocabulary(level, pageable);
  }

  @SecurityRequirement(name = "bearer-jwt")
  @GetMapping("/count")
  @Operation(summary = "Get count of vocabulary words for a given level")
  public long countVocabularyByLevel(
      @Parameter(description = "Level filter: a1, a2, b1, b2, c1, c2", required = true)
      @RequestParam Level level) {
    return vocabularyService.countByLevel(level);
  }
}
