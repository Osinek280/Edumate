package com.edumate.edumate.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VocabularyStatusCountDto {
  private long known;
  private long unknown;
  private long learning;
}
