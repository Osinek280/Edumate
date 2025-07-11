package com.edumate.edumate.entities.vocabulary;

import com.edumate.edumate.entities.books.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Vocabulary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String word;

  @Enumerated(EnumType.STRING)
  private Level level;

  private String phonetic;
  private String translation;

  @ManyToMany(mappedBy = "vocabularySet")
  @Builder.Default
  private Set<Unit> units = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Vocabulary)) return false;
    Vocabulary that = (Vocabulary) o;
    return id != null && id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  // Metody pomocnicze do zarządzania relacją
  public void addUnit(Unit unit) {
    this.units.add(unit);
    unit.getVocabularySet().add(this);
  }

  public void removeUnit(Unit unit) {
    this.units.remove(unit);
    unit.getVocabularySet().remove(this);
  }
}