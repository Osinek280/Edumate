package com.edumate.edumate.entities.books;

import com.edumate.edumate.entities.vocabulary.Vocabulary;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Unit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  private Integer unitNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id")
  @JsonBackReference
  private Book book;

  @Lob
  @Column(name = "wordlist_image")
  private byte[] wordlistImage;

  @ManyToMany
  @JoinTable(
      name = "unit_vocabulary",
      joinColumns = @JoinColumn(name = "unit_id"),
      inverseJoinColumns = @JoinColumn(name = "vocabulary_id")
  )
  @Builder.Default
  private Set<Vocabulary> vocabularySet = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Unit)) return false;
    Unit unit = (Unit) o;
    return id != null && id.equals(unit.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public void addVocabulary(Vocabulary vocabulary) {
    this.vocabularySet.add(vocabulary);
    vocabulary.getUnits().add(this);
  }

  public void removeVocabulary(Vocabulary vocabulary) {
    this.vocabularySet.remove(vocabulary);
    vocabulary.getUnits().remove(this);
  }

  public void setBook(Book book) {
    if (this.book != null) {
      this.book.getUnits().remove(this);
    }
    this.book = book;
    if (book != null) {
      book.getUnits().add(this);
    }
  }
}
