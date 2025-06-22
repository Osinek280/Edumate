package com.edumate.edumate.entities.vocabulary;

import com.edumate.edumate.entities.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserVocabulary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private AppUser user;

  @ManyToOne
  private Vocabulary vocabulary;

  @Enumerated(EnumType.STRING)
  private LearningStatus status;
}
