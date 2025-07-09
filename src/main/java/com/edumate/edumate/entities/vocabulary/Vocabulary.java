package com.edumate.edumate.entities.vocabulary;

import com.edumate.edumate.entities.books.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vocabulary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String word;
//  private String wordClass;
  @Enumerated(EnumType.STRING)
  private Level level;
  private String phonetic;
  private String translation;
  @ManyToMany
  @JoinTable(
      name = "unit_vocabulary",
      joinColumns = @JoinColumn(name = "vocabulary_id"),
      inverseJoinColumns = @JoinColumn(name = "unit_id")
  )
  private Set<Unit> units;
}
